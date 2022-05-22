package cn.element.ioc.test.demo;

import cn.element.ioc.context.ApplicationContext;
import cn.element.ioc.context.ConfigurableApplicationContext;
import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
import cn.element.ioc.test.demo.bean.Eatable;
import cn.element.ioc.test.demo.bean.Person;
import cn.element.ioc.test.demo.bean.User;
import cn.element.ioc.test.demo.event.LoginEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

@Slf4j
public class TestApplicationContext {
    
    private ApplicationContext context;
    
    @BeforeEach
    public void before() {
        context = new ClassPathXmlApplicationContext("classpath:application.xml");
    }

    /**
     * 测试DI功能
     */
    @Test
    public void testContext() {
        User user = context.getBean("user", User.class);
        user.eat();
    }

    /**
     * 测试PropertyPlaceHolder功能
     */
    @Test
    public void testPlaceHolder() {
        Eatable user = context.getBean("user", User.class);
        System.out.println(user);
    }

    /**
     * 测试生命周期钩子函数功能
     */
    @Test
    public void testPostProcessor() {
        ConfigurableApplicationContext context0 = (ConfigurableApplicationContext) context; 
        Person person = context0.getBean("person", Person.class);
        log.debug("生命周期 第四阶段 ====> 使用Bean {}", person);
        context0.close();
    }

    /**
     * 测试容器事件发布功能
     */
    @Test
    public void testPublishEvent() {
        long id = new Random().nextLong();
        id = id >= 0 ? id : -id;
        String message = "用户正在登录...";
        ConfigurableApplicationContext context0 = (ConfigurableApplicationContext) context; 
        context0.publishEvent(new LoginEvent(context, id, message));
        context0.registerShutdownHook();
    }
    
    

}
