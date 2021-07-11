package cn.element.test;

import cn.element.core.BeansException;
import cn.element.core.beans.factory.BeanDefinition;
import cn.element.core.beans.support.DefaultListableBeanFactory;
import org.junit.Test;

public class TestBean {

    /**
     * 测试BeanFactory
     */
    @Test
    public void test01() {

//        BeanFactory beanFactory = new BeanFactory();  //初始化BeanFactory
//
//        BeanDefinition beanDefinition = new BeanDefinition(new UserService());  //注册bean
//
//        beanFactory.registerBeanDefinition("userService",beanDefinition);
//
//        UserService userService = (UserService) beanFactory.getBean("userService");
//
//        userService.queryUserInfo();
    }

    @Test
    public void test02() throws BeansException {

        //1.初始化BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //2.注册bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);

        beanFactory.registerBeanDefinition("userService", beanDefinition);

        //3.第一次获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");

        userService.queryUserInfo();

        //4.第二次获取bean from Singleton
        UserService userService1 = (UserService) beanFactory.getBean("userService");

        userService1.queryUserInfo();
    }

    @Test
    public void test03() throws BeansException {

        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. 注入bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 3.获取bean
        UserService userService = (UserService) beanFactory.getBean("userService", "小傅哥");
        userService.queryUserInfo();
    }

}
