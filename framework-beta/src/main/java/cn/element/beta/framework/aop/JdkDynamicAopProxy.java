package cn.element.beta.framework.aop;

import cn.element.beta.framework.aop.interceptor.MethodInvocation;
import cn.element.beta.framework.aop.support.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    
    private final AdvisedSupport support;

    public JdkDynamicAopProxy(AdvisedSupport support) {
        this.support = support;
    }

    /**
     * 把原生的对象传进来
     */
    @Override
    public Object getProxy() {
        return getProxy(support.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader loader) {
        return Proxy.newProxyInstance(loader, support.getTargetClass().getInterfaces(), this);
    }

    /**
     * invoke()方法是执行代理的关键入口
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 将每一个JoinPoint也就是被代理的业务方法(Method)封装成一个拦截器,组合成一个拦截器链
        List<Object> list = support.getInterceptorsAndDynamicInterceptorAdvice(method, support.getTargetClass());
        
        // 交给拦截器链MethodInvocation的proceed()方法执行
        MethodInvocation invocation = new MethodInvocation(proxy, support.getTarget(), method, args, support.getTargetClass(), list);
        return invocation.proceed();
    }
}
