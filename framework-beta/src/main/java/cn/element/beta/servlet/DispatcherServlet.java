package cn.element.beta.servlet;

import cn.element.ioc.beans.factory.annotation.Autowired;
import cn.element.ioc.stereotype.Controller;
import cn.element.web.bind.annotation.RequestMapping;
import cn.element.ioc.stereotype.Service;
import cn.element.web.bind.annotation.RequestParam;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    private final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    /**
     * 保存application.properties配置文件中的内容
     */
    private final Properties contextConfig = new Properties();

    /**
     * 保存扫描的所有的类名
     */
    private final List<String> classNames = new ArrayList<>();

    /**
     * ioc容器
     */
    private final Map<String, Object> ioc = new HashMap<>();

    /**
     * 保存url和Method的关系
     * 使用委派模式实现路径名和方法名之间的映射
     */
    private final List<Handler> handlerMapping = new ArrayList<>();
    

    @Override
    public void init(ServletConfig config) {
        // 1.加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        
        // 2.扫描相关的类
        doScanner(contextConfig.getProperty("scanPackage"));
        
        // 3.初始化扫描到的类,并且将它们放入IoC容器中
        doInstance();
        
        // 4.完成依赖注入
        doAutowired();
        
        // 5.初始化HandlerMapping
        initHandlerMapping();

        System.out.println("MVC Framework Has Initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
     * 匹配URL
     */
    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Handler handler = getHandler(request);
        
        if (handler == null) {
            response.getWriter().write("404 NOT FOUND");
            return;
        }
        
        // 获得方法的形参列表
        Class<?>[] paramTypes = handler.method.getParameterTypes();
        Object[] paramValues = new Object[paramTypes.length];
        Map<String, String[]> params = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String value = Arrays.toString(entry.getValue())
                                 .replaceAll("\\[|\\]", "")
                                 .replaceAll("\\s", ",");
            
            if (!handler.paramIndexMapping.containsKey(entry.getKey())) {
                continue;
            }
            
            int index = handler.paramIndexMapping.get(entry.getKey());
            paramValues[index] = convert(paramTypes[index], value);
        }
        
        if (handler.paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int reqIndex = handler.paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = request;
        }
        
        if (handler.paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = handler.paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = response;
        }
        
        Object returned = handler.method.invoke(handler.controller, paramValues);
        
        if (returned == null) {
            return;
        }
        
        response.getWriter().write(returned.toString());
    }

    /**
     * 直接通过类路径找到配置文件所在的路径
     * 并且将其读取出来放到Properties对象中
     * 相当于将scanPackage保存到了内存中
     */
    private void doLoadConfig(String location) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location);

        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 扫描相关的类
     * scanPackage是包路径
     * 需要转换为文件路径
     */
    private void doScanner(String scanPackage) {
        URL url = this.getClass()
                      .getClassLoader()
                      .getResource("/" + scanPackage.replaceAll("\\.", "/"));

        assert url != null;
        File classPath = new File(url.getFile());

        for (File file : Objects.requireNonNull(classPath.listFiles())) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (file.getName().endsWith(".class")) {
                    String className = (scanPackage + "." + file.getName().replace(".class", ""));
                    classNames.add(className);
                }
            }
        }
    }

    /**
     * 实例化Bean
     * 需要判断类是否加了@Controller @Service @Component注解
     * 默认类名是小写的
     */
    private void doInstance() {
        // 初始化,为DI做准备
        if (CollectionUtil.isEmpty(classNames)) {
            return;
        }

        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                
                if (clazz.isAnnotationPresent(Controller.class)) {
                    Object instance = clazz.newInstance();
                    String beanName = StrUtil.lowerFirst(clazz.getName());
                    
                    ioc.put(beanName, instance);
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = clazz.getAnnotation(Service.class);
                    String beanName = service.value().trim();
                    
                    if (StrUtil.isEmpty(beanName)) {
                        beanName = StrUtil.lowerFirst(clazz.getName());
                    }

                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);
                    
                    // 根据类型自动赋值,这是投机取巧的方式
                    for (Class<?> c : clazz.getInterfaces()) {
                        if (ioc.containsKey(c.getName())) {
                            throw new Exception("The \" " + c.getName() + " \" has existed!");
                        }
                        
                        // 把接口类型直接当成key
                        ioc.put(c.getName(), instance);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自动进行依赖注入
     * 获取所有的字段,包括private protected default类型的
     * 正常来说,普通的OOP编程只能获得public类型的字段
     */
    private void doAutowired() {
        if (CollectionUtil.isEmpty(ioc)) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired wired = field.getAnnotation(Autowired.class);
                    String beanName = wired.value().trim();
                    
                    if (StrUtil.isEmpty(beanName)) {
                        beanName = StrUtil.lowerFirst(field.getType().getName());
                    }
                    
                    field.setAccessible(true);

                    try {
                        // 利用反射机制动态给字段赋值
                        field.set(entry.getValue(), ioc.get(beanName));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        ioc.forEach((k, v) -> System.out.println(k + "=" + v));
    }

    /**
     * 初始化url和Method的一对一关系
     */
    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }
            
            // 保存在类上面的@RequestMapping("/")
            String baseUrl = "";
            
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                baseUrl = requestMapping.value();
            }
            
            // 默认获取所有的public类型的方法
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String regex = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);
                    
                    handlerMapping.add(new Handler(entry.getValue(), method, pattern));
                    System.out.println("Mapped : " + regex + " , " + method.getName());
                }
            }
        }
    }
    
    private Handler getHandler(HttpServletRequest request) {
        if (handlerMapping.isEmpty()) {
            return null;
        }
        
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        logger.info("请求路由转发路径: {}", url);

        for (Handler handler : handlerMapping) {
            try {
                Matcher matcher = handler.pattern.matcher(url);

                if (matcher.matches()) {
                    return handler;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    private Object convert(Class<?> type, String value) {
        if (type == Integer.class) {
            return Integer.parseInt(value);
        }
        
        return value;
    }

    /**
     * 记录Controller中的RequestMapping和Method的关系
     */
    private static class Handler {
        protected Object controller;        // 保存方法对应的实例
        protected Method method;            // 保存映射的方法
        protected Pattern pattern;
        protected final Map<String, Integer> paramIndexMapping;   // 参数顺序

        public Handler(Object controller, Method method, Pattern pattern) {
            this.controller = controller;
            this.method = method;
            this.pattern = pattern;
            this.paramIndexMapping = new HashMap<>();
            putParamIndexMapping(method);
        }
        
        private void putParamIndexMapping(Method method) {
            // 提取方法中加了注解的参数
            Annotation[][] pa = method.getParameterAnnotations();

            for (int i = 0; i < pa.length; i++) {
                for (Annotation a : pa[i]) {
                    if (a instanceof RequestParam) {
                        String paramName = ((RequestParam) a).value();
                        
                        if (!StrUtil.isBlank(paramName)) {
                            paramIndexMapping.put(paramName, i);
                        }
                    }
                }
            }
            
            // 提取方法中的request和response参数
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
