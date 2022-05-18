package cn.element.spring.test;

import cn.element.spring.event.UserRegisterEvent;
import cn.element.spring.pojo.Book;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

public class TestContext {
    
    private ApplicationContext context;
    
    @Before
    public void before() {
        context = new ClassPathXmlApplicationContext("application.xml");
    }
    
    @Test
    public void testContext() {
        Book book = context.getBean(Book.class);
        System.out.println(book);
    }

    /**
     * 测试从DefaultSingletonBeanRegistry中获取单例Bean
     */
    @Test
    public void testSingletonBeans() throws NoSuchFieldException, IllegalAccessException {
        ConfigurableApplicationContext context0 = (ConfigurableApplicationContext) context;
        Field field = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        field.setAccessible(true);
        ConfigurableListableBeanFactory factory = context0.getBeanFactory();
        Map<String, Object> map = (Map<String, Object>) field.get(factory);
        map.forEach((k, v) -> System.out.println(k + "=" + v));
    }

    /**
     * 测试多语言翻译功能
     */
    @Test
    public void testMessageResource() {
        System.out.println(context.getMessage("hi", null, Locale.CHINA));
        System.out.println(context.getMessage("hi", null, Locale.ENGLISH));
    }

    /**
     * 测试获取环境变量
     */
    @Test
    public void testEnvironment() {
        System.out.println(context.getEnvironment().getProperty("JAVA_HOME"));
    }

    /**
     * 测试事件发布
     */
    @Test
    public void testApplicationEventPublisher() {
        context.publishEvent(new UserRegisterEvent(context));
    }

}
