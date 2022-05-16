package cn.element.ioc.test.demo;

import cn.element.ioc.context.ApplicationContext;
import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Test;

public class TestApplicationContext {
    
    @Test
    public void testContext() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        User user = context.getBean("user", User.class);
        user.eat();
    }
    
    @Test
    public void testPlaceHolder() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        User user = context.getBean("user", User.class);
        System.out.println(user);
    }

}
