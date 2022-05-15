package cn.element.ioc.aop.framework.adapter;

import cn.element.ioc.aop.MethodAroundAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 环绕拦截器
 */
public class MethodAroundAdviceInterceptor implements MethodInterceptor {
    
    private MethodAroundAdvice advice;

    public MethodAroundAdviceInterceptor() {
        
    }

    public MethodAroundAdviceInterceptor(MethodAroundAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return advice.around(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
    }

    public MethodAroundAdvice getAdvice() {
        return advice;
    }

    public void setAdvice(MethodAroundAdvice advice) {
        this.advice = advice;
    }
}
