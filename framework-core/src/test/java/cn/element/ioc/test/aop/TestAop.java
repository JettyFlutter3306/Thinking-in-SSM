package cn.element.ioc.test.aop;

import cn.element.ioc.aop.AdvisedSupport;
import cn.element.ioc.aop.TargetSource;
import cn.element.ioc.aop.aspectj.AspectJExpressionPointcut;
import cn.element.ioc.aop.framework.Cglib2AopProxy;
import cn.element.ioc.aop.framework.JdkDynamicAopProxy;
import org.junit.Test;

import java.lang.reflect.Method;

public class TestAop {

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
