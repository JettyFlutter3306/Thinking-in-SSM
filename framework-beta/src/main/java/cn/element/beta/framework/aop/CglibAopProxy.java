package cn.element.beta.framework.aop;

import cn.element.beta.framework.aop.support.AdvisedSupport;

/**
 * 使用CgLib API生成代理类
 */
public class CglibAopProxy implements AopProxy {
    
    private final AdvisedSupport support;

    public CglibAopProxy(AdvisedSupport support) {
        this.support = support;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader loader) {
        return null;
    }
}
