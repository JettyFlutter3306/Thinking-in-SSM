package cn.element.ioc.aop;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * 包装切面通知信息
 * AdvisedSupport，主要是用于把代理、拦截、匹配的各项属性包装到一个类中，方
 * 便在 Proxy 实现类进行使用。这和你的业务开发中包装入参是一个道理
 */
public class AdvisedSupport {

    /**
     * 被代理的对象
     * TargetSource，是一个目标对象，在目标对象类中提供 Object 入参属性，以及获
     * 取目标类 TargetClass 信息。
     */
    private TargetSource target;

    // 方法拦截器
    private MethodInterceptor interceptor;

    /**
     * 方法匹配器
     * 是一个匹配方法的操作，这个对象由 AspectJExpressionPointcut
     * 提供服务。
     */
    private MethodMatcher matcher;

    public TargetSource getTargetSource() {
        return target;
    }

    public void setTargetSource(TargetSource target) {
        this.target = target;
    }

    public MethodInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public MethodMatcher getMatcher() {
        return matcher;
    }

    public void setMatcher(MethodMatcher matcher) {
        this.matcher = matcher;
    }
}
