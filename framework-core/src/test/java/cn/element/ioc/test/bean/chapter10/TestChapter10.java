package cn.element.ioc.test.bean.chapter10;

import org.openjdk.jol.info.ClassLayout;
import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

public class TestChapter10 {

    /**
     * 单例测试
     */
    @Test
    public void testPrototype() {
        // 1.初始化BeanFactory
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application10.xml");
        context.registerShutdownHook();

        // 2.获取Bean对象调用方法
        UserService userService01 = context.getBean("userService", UserService.class);
        UserService userService02 = context.getBean("userService", UserService.class);

        // 3.配置scope="prototype/singleton"
        System.out.println(userService01);
        System.out.println(userService02);

        // 4.打印十六进制哈希值
        System.out.println(userService01 + ": " + Integer.toHexString(userService01.hashCode()));
        System.out.println(ClassLayout.parseInstance(userService01).toPrintable());
    }

    @Test
    public void testFactoryBean() {
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application10.xml");
        applicationContext.registerShutdownHook();

        // 2. 调用代理方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }

}
