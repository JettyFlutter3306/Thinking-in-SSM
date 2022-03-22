package cn.element.beta.framework.aop.aspect;

import cn.element.beta.framework.aop.interceptor.MethodInterceptor;
import cn.element.beta.framework.aop.interceptor.MethodInvocation;

import java.lang.reflect.Method;

public class MethodBeforeAdviceInterceptor extends AbstractAspectJAdvice implements Advice, MethodInterceptor {

    private JoinPoint joinPoint;

    public MethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    private void before(Method method, Object[] args, Object target) throws Throwable {
        //传送了给织入参数
        //method.invoke(target);
        super.invokeAdviceMethod(joinPoint, null, null);

    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        //从被织入的代码中才能拿到，JoinPoint
        joinPoint = mi;
        before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }
}
