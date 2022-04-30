package cn.element.ioc.test.stage15;

import cn.element.ioc.context.ApplicationContext;
import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Test;

public class ApiTest {

    @Test
    public void testAutoProxy() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application15.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }

}
