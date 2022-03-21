package cn.element.ioc.aop.framework;

import cn.element.ioc.aop.AdvisedSupport;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 基于 JDK 实现的代理类，需要实现接口 AopProxy、InvocationHandler，这样就可
 * 以把代理对象 getProxy 和反射调用方法 invoke 分开处理了
 * 
 * getProxy 方法中的是代理一个对象的操作，需要提供入参 ClassLoader、
 * AdvisedSupport、和当前这个类 this，因为这个类提供了 invoke 方法。
 * 
 * invoke 方法中主要处理匹配的方法后，使用用户自己提供的方法拦截实现，做反
 * 射调用 methodInterceptor.invoke
 * 
 * 这里还有一个 ReflectiveMethodInvocation，其他它就是一个入参的包装信息，提
 * 供了入参对象：目标对象、方法、入参
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    
    private final AdvisedSupport support;

    public JdkDynamicAopProxy(AdvisedSupport support) {
        this.support = support;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(
                   Thread.currentThread().getContextClassLoader(), 
                   support.getTargetSource().getInterfaces(),
                   this
               );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (support.getMatcher().matches(method, support.getTargetSource().getTarget().getClass())) {
            MethodInterceptor interceptor = support.getInterceptor();
            return interceptor.invoke(new ReflectiveMethodInvocation(support.getTargetSource().getTarget(), method, args));
        }
        
        return method.invoke(support.getTargetSource().getTarget(), args);
    }
}