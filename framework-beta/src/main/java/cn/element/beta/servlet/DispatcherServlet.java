package cn.element.beta.servlet;

import cn.element.ioc.annotation.AutoWired;
import cn.element.mvc.annotation.Controller;
import cn.element.mvc.annotation.RequestMapping;
import cn.element.mvc.annotation.Service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class DispatcherServlet extends HttpServlet {

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
    private final Map<String, Method> handlerMapping = new HashMap<>();
    

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
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exception, Detail : " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        System.out.println(url);
        System.out.println(contextPath);
        url = url.replaceAll(contextPath, "").replaceAll("/+", "/");
        System.out.println(url);
                
        if (!handlerMapping.containsKey(url)) {
            response.getWriter().write("404 NOT FOUND");
            return;
        }

        Method method = handlerMapping.get(url);
        Map<String, String[]> params = request.getParameterMap();
        
        String beanName = StrUtil.lowerFirst(method.getDeclaringClass().getName());
        method.invoke(ioc.get(beanName), request, response, params.get("name")[0]);
        System.out.println(method.getName());
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
                if (field.isAnnotationPresent(AutoWired.class)) {
                    AutoWired wired = field.getAnnotation(AutoWired.class);
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
                    String url = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");
                    
                    handlerMapping.put(url, method);
                    System.out.println("Mapped : " + url + " , " + method.getName());
                }
            }
        }
    }
}
