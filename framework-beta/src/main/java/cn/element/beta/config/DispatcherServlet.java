package cn.element.beta.config;

import cn.element.ioc.annotation.AutoWired;
import cn.element.mvc.annotation.Controller;
import cn.element.mvc.annotation.RequestMapping;
import cn.element.mvc.annotation.RequestParam;
import cn.element.mvc.annotation.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DispatcherServlet extends HttpServlet {

    private static final String LOCATION = "contextConfigLocation";

    private final Properties p = new Properties();

    private final List<String> classNames = new ArrayList<>();

    private final Map<String, Object> ioc = new HashMap<>();

    // 保存所有的Url和方法的映射关系
    private final List<Handler> handlerMapping = new ArrayList<>();

    public DispatcherServlet() {
        super();
    }

    /**
     * 初始化，加载配置文件
     */
    public void init(ServletConfig config) {
        //1、加载配置文件
        doLoadConfig(config.getInitParameter(LOCATION));

        //2、扫描所有相关的类
        doScanner(p.getProperty("scanPackage"));

        //3、初始化所有相关类的实例，并保存到IOC容器中
        doInstance();

        //4、依赖注入
        doAutowired();

        //5、构造HandlerMapping
        initHandlerMapping();

        //6、等待请求，匹配URL，定位方法， 反射调用执行
        //调用doGet或者doPost方法

        //提示信息
        System.out.println("MVC framework is init");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * 执行业务处理
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            doDispatch(req, resp); //开始始匹配到对应的方方法
        } catch (Exception e) {
            //如果匹配过程出现异常，将异常信息打印出去
            resp.getWriter().write("500 Exception,Details:\r\n" + Arrays.toString(e.getStackTrace())
                                                                        .replaceAll("\\[|\\]", "")
                                                                        .replaceAll(",\\s", "\r\n"));
        }
    }


    /**
     * 递归扫描出所有Class文件
     */
    private void doScanner(String packageName) {
        //将所有的包路径转换为文件路径
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));

        assert url != null;
        File dir = new File(url.getFile());

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            // 如果是文件夹，继续递归
            if (file.isDirectory()) {
                doScanner(packageName + "." + file.getName());
            } else {
                classNames.add(packageName + "." + file.getName().replace(".class", "").trim());
            }
        }
    }

    private void doLoadConfig(String location) {
        InputStream fis = null;

        try {
            fis = this.getClass().getClassLoader().getResourceAsStream(location);
            //1、读取配置文件
            p.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doInstance() {
        if (classNames.size() == 0) {
            return;
        }

        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);

                if (clazz.isAnnotationPresent(Controller.class)) {
                    //默认将首字母小写作为beanName
                    String beanName = lowerFirst(clazz.getSimpleName());
                    ioc.put(beanName, clazz.newInstance());
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = clazz.getAnnotation(Service.class);
                    String beanName = service.value();

                    // 如果用户设置了名字，就用用户自己设置
                    if (!"".equals(beanName.trim())) {
                        ioc.put(beanName, clazz.newInstance());
                        continue;
                    }

                    // 如果自己没设，就按接口类型创建一个实例
                    Class<?>[] interfaces = clazz.getInterfaces();

                    for (Class<?> i : interfaces) {
                        ioc.put(i.getName(), clazz.newInstance());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //拿到实例对象中的所有属性
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(AutoWired.class)) {
                    continue;
                }

                AutoWired autowired = field.getAnnotation(AutoWired.class);
                String beanName = autowired.value().trim();

                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }

                field.setAccessible(true); //设置私有属性的访问权限

                try {
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }

            String url = "";
            //获取Controller的url配置
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                url = requestMapping.value();
            }

            //获取Method的url配置
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                //没有加RequestMapping注解的直接忽略
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }

                //映射URL
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String regex = ("/" + url + requestMapping.value()).replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);
                handlerMapping.add(new Handler(pattern, entry.getValue(), method));
                System.out.println("mapping " + regex + "," + method);
            }
        }

    }


    /**
     * 首字母小母
     */
    private String lowerFirst(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    /**
     * 匹配URL
     */
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Handler handler = getHandler(req);

            if (handler == null) {
                //如果没有匹配上，返回404错误
                resp.getWriter().write("404 Not Found");
                return;
            }

            //获取方法的参数列表
            Class<?>[] paramTypes = handler.method.getParameterTypes();

            //保存所有需要自动赋值的参数值
            Object[] paramValues = new Object[paramTypes.length];

            Map<String, String[]> params = req.getParameterMap();
            for (Map.Entry<String, String[]> param : params.entrySet()) {
                String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");

                //如果找到匹配的对象，则开始填充参数值
                if (!handler.paramIndexMapping.containsKey(param.getKey())) {
                    continue;
                }

                int index = handler.paramIndexMapping.get(param.getKey());
                paramValues[index] = convert(paramTypes[index], value);
            }

            // 设置方法中的request和response对象
            int reqIndex = handler.paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;
            int respIndex = handler.paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;
            handler.method.invoke(handler.controller, paramValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler getHandler(HttpServletRequest req) {
        if (handlerMapping.isEmpty()) {
            return null;
        }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (Handler handler : handlerMapping) {
            try {
                Matcher matcher = handler.pattern.matcher(url);
                //如果没有匹配上继续下一个匹配
                if (!matcher.matches()) {
                    continue;
                }

                return handler;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    //url传过来的参数都是String类型的，HTTP是基于字符串协议
    //只需要把String转换为任意类型就好
    private Object convert(Class<?> type, String value) {
        if (Integer.class == type) {
            return Integer.valueOf(value);
        }
        //如果还有double或者其他类型，继续加if
        //这时候，我们应该想到策略模式了
        //在这里暂时不实现，希望小伙伴自己来实现
        return value;
    }

    /**
     * Handler记录Controller中的RequestMapping和Method的对应关系
     * 内部类
     */
    private static class Handler {
        protected Object controller;    //保存方法对应的实例
        protected Method method;        //保存映射的方法
        protected Pattern pattern;
        protected Map<String, Integer> paramIndexMapping;    //参数顺序

        /**
         * 构造一个Handler基本的参数
         */
        protected Handler(Pattern pattern, Object controller, Method method) {
            this.controller = controller;
            this.method = method;
            this.pattern = pattern;

            paramIndexMapping = new HashMap<>();
            putParamIndexMapping(method);
        }

        private void putParamIndexMapping(Method method) {
            //提取方法中加了注解的参数
            Annotation[][] pa = method.getParameterAnnotations();
            for (int i = 0; i < pa.length; i++) {
                for (Annotation a : pa[i]) {
                    if (a instanceof RequestParam) {
                        String paramName = ((RequestParam) a).value();
                        if (!"".equals(paramName.trim())) {
                            paramIndexMapping.put(paramName, i);
                        }
                    }
                }
            }

            //提取方法中的request和response参数
            Class<?>[] paramsTypes = method.getParameterTypes();
            for (int i = 0; i < paramsTypes.length; i++) {
                Class<?> type = paramsTypes[i];
                if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                    paramIndexMapping.put(type.getName(), i);
                }
            }
        }
    }
}
