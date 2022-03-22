package cn.element.beta.framework.aop.aspect;

import cn.element.beta.framework.aop.interceptor.MethodInterceptor;
import cn.element.beta.framework.aop.interceptor.MethodInvocation;

import java.lang.reflect.Method;

public class AfterThrowingAdviceInterceptor extends AbstractAspectJAdvice implements Advice, MethodInterceptor {

    private String throwingName;

    public AfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Throwable e) {
            invokeAdviceMethod(mi, null, e.getCause());
            throw e;
        }
    }

    public void setThrowName(String throwName) {
        this.throwingName = throwName;
    }
}
