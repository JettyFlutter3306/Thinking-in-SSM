package cn.element.ioc.test.stage14;

import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
import cn.element.ioc.test.stage14.bean.IUserService;
import org.junit.jupiter.api.Test;

public class ApiTest {

    @Test
    public void testScan() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application14.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }
}
