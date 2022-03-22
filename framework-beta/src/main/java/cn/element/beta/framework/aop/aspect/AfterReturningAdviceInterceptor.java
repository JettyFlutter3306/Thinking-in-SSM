package cn.element.beta.framework.aop.aspect;

import cn.element.beta.framework.aop.interceptor.MethodInterceptor;
import cn.element.beta.framework.aop.interceptor.MethodInvocation;

import java.lang.reflect.Method;

public class AfterReturningAdviceInterceptor extends AbstractAspectJAdvice implements Advice, MethodInterceptor {

    private JoinPoint joinPoint;

    public AfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        joinPoint = mi;
        afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }

    private void afterReturning(Object retVal, Method method, Object[] arguments, Object aThis) throws Throwable {
        super.invokeAdviceMethod(this.joinPoint, retVal, null);
    }
}
