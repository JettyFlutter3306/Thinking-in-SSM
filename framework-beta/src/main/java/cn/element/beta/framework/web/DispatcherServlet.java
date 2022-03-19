package cn.element.beta.framework.web;

import cn.element.beta.framework.context.ApplicationContext;
import cn.element.ioc.stereotype.Controller;
import cn.element.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * DispatcherServlet作为MVC启动的入口
 */
@Slf4j
public class DispatcherServlet extends HttpServlet {
    
    private final String LOCATION = "contextConfigLocation";
    
    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    
    private final Map<HandlerMapping, HandlerAdapter> handlerAdapterMap = new HashMap<>();
    
    private final List<ViewResolver> viewResolvers = new ArrayList<>();
    
    private ApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 相当于把IoC容器初始化了
        context = new ApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    /**
     * 初始化MVC九大组件,有九种策略
     * 针对每个用户请求,都会经过一些处理策略处理,最终才能有结果输出
     * 每种策略可以自定义干预,但是最终的结果都一致
     */
    protected void initStrategies(ApplicationContext context) {
        // 1.文件上传组件,如果请求是MultipartResolver,将通过MultipartResolver进行文件上传解析
        initMultipartResolver(context);
        
        // 2.国际化解析器
        initLocaleResolver(context);
        
        // 3.主题解析
        initThemeResolver(context);
        
        // 4.路径方法映射器
        initHandlerMappings(context);
        
        // 5.处理器适配
        initHandlerAdapters(context);
        
        // 6.请求异常处理器
        initHandlerExceptionResolvers(context);
        
        // 7.请求视图解析器
        initRequestToViewNameTranslator(context);
        
        // 8.视图解析器
        initViewResolvers(context);
        
        // 9.Flash映射管理器
        initFlashMapManager(context);
    }

    /**
     * 初始化FlashMap
     */
    private void initFlashMapManager(ApplicationContext context) {
        
    }

    /**
     * 初始化RequestToViewNameTranslator
     */
    private void initRequestToViewNameTranslator(ApplicationContext context) {
        
    }

    /**
     * 初始化HandlerExceptionResolver
     * 用于处理请求出现异常的情况
     */
    private void initHandlerExceptionResolvers(ApplicationContext context) {
        
    }

    /**
     * 初始化ThemeResolver
     * 用于切换皮肤
     */
    private void initThemeResolver(ApplicationContext context) {
        
    }

    /**
     * 初始化LocaleResolver
     */
    private void initLocaleResolver(ApplicationContext context) {
        
    }

    /**
     * 初始化MultipartResolver
     * 用于多文件上传
     */
    private void initMultipartResolver(ApplicationContext context) {
        
    }

    /**
     * 初始化HandlerMapping
     * 用于路径和方法之间的映射
     * 数据结构: Map[String, Method]
     *          put(url, method)
     */
    private void initHandlerMappings(ApplicationContext context) {
        // 首先从容器中获取所有的实例
        String[] names = context.getBeanDefinitionNames();

        try {
            for (String name : names) {
                // 到了MVC层,对外提供的方法只有一个getBean()方法
                Object controller = context.getBean(name);
                Class<?> clazz = controller.getClass();
                
                if (clazz.isAnnotationPresent(Controller.class)) {
                    String baseUrl = "";
                    
                    if (clazz.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
                        baseUrl = mapping.value();
                    }
                    
                    // 扫描所有的public类型的方法
                    Method[] methods = clazz.getMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                            String regex = ("/" + baseUrl + mapping.value()
                                                                   .replaceAll("\\*", ".*")
                                                                   .replaceAll("/+", "/"));
                            Pattern pattern = Pattern.compile(regex);
                            handlerMappings.add(new HandlerMapping(pattern, controller, method));
                            log.info("Mapping: {} - {}", regex, method);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化HandlerAdapter
     * 用于动态匹配Method参数,包括类型转换,动态赋值
     * 在初始化阶段,能做的就是,将这些参数的名字或者类型按一定的顺序保存下来
     * 因为后面用反射调用的时候,传的形参是一个数组
     * 可以通过记录这些参数的位置index,逐个从数组中取值,这样就和参数的顺序无关了
     */
    private void initHandlerAdapters(ApplicationContext context) {
        for (HandlerMapping mapping : handlerMappings) {
            // 每个方法有一个参数列表,这里保存的是形参列表
            handlerAdapterMap.put(mapping, new HandlerAdapter());
        }
    }

    /**
     * 初始化ViewResolver
     * 用于视图解析
     * 在页面中输入http://localhost/index.html
     * 解决页面名字和模板文件关联的问题
     */
    private void initViewResolvers(ApplicationContext context) {
        String templateRoot = context.getConfig().getProperty("templates");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);

        for (File file : templateRootDir.listFiles()) {
            viewResolvers.add(new ViewResolver(templateRoot));
        }
    }
    
    
    
    
}
