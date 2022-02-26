//package cn.element.ioc.test;
//
//import cn.element.ioc.beans.BeansException;
//import cn.element.ioc.beans.PropertyValue;
//import cn.element.ioc.beans.PropertyValues;
//import cn.element.ioc.beans.factory.config.BeanDefinition;
//import cn.element.ioc.beans.factory.config.BeanReference;
//import cn.element.ioc.beans.factory.support.DefaultListableBeanFactory;
//import cn.element.ioc.beans.factory.xml.XmlBeanDefinitionReader;
//import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
//import cn.element.ioc.test.bean.UserDao;
//import cn.element.ioc.test.bean.UserService;
//import cn.element.ioc.test.common.IBeanFactoryPostProcessor;
//import cn.element.ioc.test.common.IBeanPostProcessor;
//import org.junit.Test;
//
//public class TestBean {
//
//    @Test
//    public void testBeanFactory() throws BeansException {
//        //1.初始化BeanFactory
//        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//
//        //2.注册bean
//        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
//        beanFactory.registerBeanDefinition("userService", beanDefinition);
//
//        //3.第一次获取bean
//        UserService userService = (UserService) beanFactory.getBean("userService");
//        userService.queryUserInfo();
//
//        //4.第二次获取bean from Singleton
//        UserService userService1 = (UserService) beanFactory.getBean("userService");
//        userService1.queryUserInfo();
//    }
//
////    @Test
////    public void testBeanFactory2() throws BeansException {
////        // 1.初始化 BeanFactory
////        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
////
////        // 2. 注入bean
////        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
////        beanFactory.registerBeanDefinition("userService", beanDefinition);
////
////        // 3.获取bean
////        UserService userService = (UserService) beanFactory.getBean("userService", "小傅哥");
////        userService.queryUserInfo();
////    }
////
////    @Test
////    public void testBeanFactory3() throws BeansException {
////        // 1.初始化 BeanFactory
////        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
////
////        // 2. UserDao 注册
////        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));
////
////        // 3. UserService 设置属性[uId、userDao]
////        PropertyValues propertyValues = new PropertyValues();
////        propertyValues.addPropertyValue(new PropertyValue("uid", "10001"));
////        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));
////
////        // 4. UserService 注入 bean
////        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
////        beanFactory.registerBeanDefinition("userService", beanDefinition);
////
////        // 5. UserService 获取 bean
////        UserService userService = (UserService) beanFactory.getBean("userService");
////        userService.queryUserInfo();
////    }
//
//    @Test
//    public void testBeanFactoryPostProcessor() {
//        // 1.初始化 BeanFactory
//        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//
//        // 2. 读取配置文件&注册 Bean
//        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
//        reader.loadBeanDefinitions("classpath:application-context.xml");
//
//        // 3. BeanDefinition 加载完成 & Bean 实例化之前，修改 BeanDefinition 的属性值
//        IBeanFactoryPostProcessor beanFactoryPostProcessor = new IBeanFactoryPostProcessor();
//        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
//
//        // 4. Bean 实例化之后，修改 Bean 属性信息
//        IBeanPostProcessor beanPostProcessor = new IBeanPostProcessor();
//        beanFactory.addBeanPostProcessor(beanPostProcessor);
//
//        // 5. 获取 Bean 对象调用方法
//        UserService userService = beanFactory.getBean("userService", UserService.class);
//        userService.queryUserInfo();
//    }
//
//    @Test
//    public void testXml() {
//        // 1.初始化 BeanFactory
//        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application-context.xml");
//        applicationContext.registerShutdownHook();
//
//        // 2. 获取 Bean 对象调用方法
//        UserService userService = applicationContext.getBean("userService", UserService.class);
//        userService.queryUserInfo();
//        System.out.println("ApplicationContextAware: " + userService.getContext());
//        System.out.println("BeanFactoryAware: " + userService.getBeanFactory());
//    }
//
//}
