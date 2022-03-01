package cn.element.ioc.test.aop;

import cn.element.ioc.aop.AdvisedSupport;
import cn.element.ioc.aop.TargetSource;
import cn.element.ioc.aop.aspectj.AspectJExpressionPointcut;
import cn.element.ioc.aop.framework.Cglib2AopProxy;
import cn.element.ioc.aop.framework.JdkDynamicAopProxy;
import cn.element.ioc.aop.framework.ProxyFactory;
import cn.element.ioc.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

public class TestAop {

    private AdvisedSupport advisedSupport;

    @Before
    public void init() {
        // 目标对象
        IUserService userService = new UserService();
        // 组装代理信息
        advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setInterceptor(new UserServiceInterceptor());
        advisedSupport.setMatcher(new AspectJExpressionPointcut("execution(* cn.element.ioc.test.aop.IUserService.*(..))"));
    }

    @Test
    public void testContext() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application12.xml");

        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }

    @Test
    public void testProxyFactory() {
        advisedSupport.setProxyTargetClass(false); // false/true，JDK动态代理、CGlib动态代理
        IUserService proxy = (IUserService) new ProxyFactory(advisedSupport).getProxy();

        System.out.println("测试结果：" + proxy.queryUserInfo());
    }

    @Test
    public void testBeforeAdvice() {
        UserServiceBeforeAdvice beforeAdvice = new UserServiceBeforeAdvice();
        MethodBeforeAdviceInterceptor interceptor = new MethodBeforeAdviceInterceptor(beforeAdvice);
        advisedSupport.setInterceptor(interceptor);

        IUserService proxy = (IUserService) new ProxyFactory(advisedSupport).getProxy();
        System.out.println("测试结果：" + proxy.queryUserInfo());
    }
    
    @Test
    public void testAop() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* cn.element.ioc.test.aop.UserService.*(..))");

        Class<UserService> clazz = UserService.class;

        Method method = clazz.getDeclaredMethod("queryUserInfo");

        System.out.println(pointcut.matches(clazz));
        System.out.println(pointcut.matches(method, clazz));
    }
    
    @Test
    public void testDynamic() {
        // 目标对象
        IUserService userService = new UserService();
        
        // 组装代理信息
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setInterceptor(new UserServiceInterceptor());
        advisedSupport.setMatcher(new AspectJExpressionPointcut("execution(* cn.element.ioc.test.aop.UserService.*(..))"));

        // 代理对象(JdkDynamicAopProxy)
        IUserService proxyJdk = (IUserService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        
        // 测试调用
        System.out.println("测试结果：" + proxyJdk.queryUserInfo());

        // 代理对象(Cglib2AopProxy)
        IUserService proxyCglib = (IUserService) new Cglib2AopProxy(advisedSupport).getProxy();
        
        // 测试调用
        System.out.println("测试结果：" + proxyCglib.register("花花"));
    }

    
}
