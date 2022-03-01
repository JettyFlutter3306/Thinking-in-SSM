package cn.element.ioc.aop.framework;

import cn.element.ioc.aop.AdvisedSupport;

/**
 * Factory for AOP proxies for programmatic use, rather than via a bean
 * factory. This class provides a simple way of obtaining and configuring
 * AOP proxies in code.
 * 
 * 其实这个代理工厂主要解决的是关于 JDK 和 Cglib 两种代理的选择问题，有了代
 * 理工厂就可以按照不同的创建需求进行控制。
 */
public class ProxyFactory {

    private final AdvisedSupport support;

    public ProxyFactory(AdvisedSupport support) {
        this.support = support;
    }
    
    public Object getProxy() {
        return createAopProxy().getProxy();
    }
    
    private AopProxy createAopProxy() {
        if (support.isProxyTargetClass()) {
            return new Cglib2AopProxy(support);
        }
        
        return new JdkDynamicAopProxy(support);
    }
}
