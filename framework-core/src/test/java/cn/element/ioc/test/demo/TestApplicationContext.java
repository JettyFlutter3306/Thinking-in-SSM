package cn.element.ioc.test.demo;

import cn.element.ioc.context.ApplicationContext;
import cn.element.ioc.context.ConfigurableApplicationContext;
import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
import cn.element.ioc.test.demo.bean.Eatable;
import cn.element.ioc.test.demo.bean.Person;
import cn.element.ioc.test.demo.bean.User;
import org.junit.jupiter.api.Test;

public class TestApplicationContext {

    /**
     * 测试DI功能
     */
    @Test
    public void testContext() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        User user = context.getBean("user", User.class);
        user.eat();
    }

    /**
     * 测试PropertyPlaceHolder功能
     */
    @Test
    public void testPlaceHolder() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        Eatable user = context.getBean("user", User.class);
        System.out.println(user);
    }

    /**
     * 测试生命周期钩子函数功能
     */
    @Test
    public void testPostProcessor() {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        Person person = context.getBean("person", Person.class);
        System.out.println(person);
        context.close();
    }

    /**
     * 测试容器事件发布功能
     */
    @Test
    public void testPublishEvent() {
        
    }

}
