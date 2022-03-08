package cn.element.ioc.test.stage12;

import cn.element.ioc.aop.AdvisedSupport;
import cn.element.ioc.aop.ClassFilter;
import cn.element.ioc.aop.MethodMatcher;
import cn.element.ioc.aop.TargetSource;
import cn.element.ioc.aop.aspectj.AspectJExpressionPointcut;
import cn.element.ioc.aop.aspectj.AspectJExpressionPointcutAdvisor;
import cn.element.ioc.aop.framework.ProxyFactory;
import cn.element.ioc.aop.framework.ReflectiveMethodInvocation;
import cn.element.ioc.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
import cn.element.ioc.test.stage12.bean.IUserService;
import cn.element.ioc.test.stage12.bean.UserService;
import cn.element.ioc.test.stage12.bean.UserServiceBeforeAdvice;
import cn.element.ioc.test.stage12.bean.UserServiceInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ApiTest {

    private AdvisedSupport advisedSupport;

    @Before
    public void init() {
        // 目标对象
        IUserService userService = new UserService();
        
        // 组装代理信息
        advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setInterceptor(new UserServiceInterceptor());
        advisedSupport.setMatcher(new AspectJExpressionPointcut("execution(* cn.element.ioc.test.stage12.bean.IUserService.*(..))"));
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
    public void testAdvisor() {
        // 目标对象
        IUserService userService = new UserService();

        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression("execution(* cn.element.ioc.test.stage12.bean.IUserService.*(..))");
        advisor.setAdvice(new MethodBeforeAdviceInterceptor(new UserServiceBeforeAdvice()));

        ClassFilter classFilter = advisor.getPointcut().getClassFilter();
        
        if (classFilter.matches(userService.getClass())) {
            AdvisedSupport advisedSupport = new AdvisedSupport();

            TargetSource targetSource = new TargetSource(userService);
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(true); // false/true，JDK动态代理、CGlib动态代理

            IUserService proxy = (IUserService) new ProxyFactory(advisedSupport).getProxy();
            System.out.println("测试结果：" + proxy.queryUserInfo());
        }
    }

    @Test
    public void testAop() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application12.xml");

        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }

    @Test
    public void testProxyMethod() {
        // 目标对象(可以替换成任何的目标对象)
        Object targetObj = new UserService();

        // AOP 代理
        IUserService proxy = (IUserService) Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(), 
            targetObj.getClass().getInterfaces(), 
            new InvocationHandler() {
                // 方法匹配器
                MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* cn.element.ioc.test.stage12.bean.IUserService.*(..))");
    
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (methodMatcher.matches(method, targetObj.getClass())) {
                        // 方法拦截器
                        MethodInterceptor methodInterceptor = invocation -> {
                            long start = System.currentTimeMillis();
                            try {
                                return invocation.proceed();
                            } finally {
                                System.out.println("监控 - Begin By AOP");
                                System.out.println("方法名称：" + invocation.getMethod().getName());
                                System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
                                System.out.println("监控 - End\r\n");
                            }
                        };
                        
                        // 反射调用
                        return methodInterceptor.invoke(new ReflectiveMethodInvocation(targetObj, method, args));
                    }
                    return method.invoke(targetObj, args);
                }
        });

        String result = proxy.queryUserInfo();
        System.out.println("测试结果：" + result);
    }

}
