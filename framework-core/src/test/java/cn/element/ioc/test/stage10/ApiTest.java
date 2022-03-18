package cn.element.ioc.test.stage10;

import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
import cn.element.ioc.test.stage10.event.CustomEvent;
import org.junit.jupiter.api.Test;

public class ApiTest {

    @Test
    public void testEvent() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application10.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, 1019129009086763L, "成功了！"));

        applicationContext.registerShutdownHook();
    }

}
