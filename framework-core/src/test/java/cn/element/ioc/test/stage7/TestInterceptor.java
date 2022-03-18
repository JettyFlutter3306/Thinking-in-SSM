package cn.element.ioc.test.stage7;

import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
import cn.element.ioc.test.stage7.bean.UserService;
import org.junit.jupiter.api.Test;

public class TestInterceptor {

    @Test
    public void testXml() {
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application7.xml");
        applicationContext.registerShutdownHook();

        // 2. 获取Bean对象调用方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
    }

    @Test
    public void testHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("close！")));
    }
}
