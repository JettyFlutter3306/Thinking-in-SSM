package cn.element.ioc.aop.framework;

import cn.element.ioc.aop.AdvisedSupport;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 基于 Cglib 使用 Enhancer 代理的类可以在运行期间为接口使用底层 ASM 字节
 * 码增强技术处理对象的代理对象生成，因此被代理类不需要实现任何接口。
 * 
 * 关于扩展进去的用户拦截方法，主要是在 Enhancer.setCallback 中处理，用户自
 * 己的新增的拦截处理。这里可以看到 DynamicAdvisedInterceptor.intercept 匹配
 * 方法后做了相应的反射操作
 */
public class Cglib2AopProxy implements AopProxy {
    
    private final AdvisedSupport support;

    public Cglib2AopProxy(AdvisedSupport support) {
        this.support = support;
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(support.getTargetSource().getTarget().getClass());
        enhancer.setInterfaces(support.getTargetSource().getInterfaces());
        enhancer.setCallback(new DynamicAdvisedInterceptor(support));
        
        return enhancer.create();
    }

    private static class DynamicAdvisedInterceptor implements MethodInterceptor {
        
        private final AdvisedSupport support;

        public DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.support = advised;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(
                    support.getTargetSource().getTarget(), 
                    method, 
                    objects, 
                    methodProxy
            );
            
            if (support.getMatcher().matches(method, support.getTargetSource().getTarget().getClass())) {
                return support.getInterceptor().invoke(methodInvocation);
            }
            
            return methodInvocation.proceed();
        }
    }

    private static class CglibMethodInvocation extends ReflectiveMethodInvocation {

        private final MethodProxy methodProxy;

        public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
            super(target, method, arguments);
            this.methodProxy = methodProxy;
        }

        @Override
        public Object proceed() throws Throwable {
            return this.methodProxy.invoke(this.target, this.args);
        }
    }
}
