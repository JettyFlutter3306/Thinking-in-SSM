package cn.element.ioc.test.event;

import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

public class TestEvent {

    @Test
    public void test() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application11.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, 1019129009086763L, "成功了！"));
        applicationContext.registerShutdownHook();
    }

}
