package cn.element.ioc.aop.framework.adapter;

import cn.element.ioc.aop.MethodBeforeAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * MethodBeforeAdviceInterceptor 实现了 MethodInterceptor 接口，在 invoke 方
 * 法中调用 advice 中的 before 方法，传入对应的参数信息。
 * 
 * 而这个 advice.before 则是用于自己实现 MethodBeforeAdvice 接口后做的相应处
 * 理。其实可以看到具体的 MethodInterceptor 实现类，其实和我们之前做的测试
 * 是一样的，只不过现在交给了 Spring 来处理
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {
    
    private MethodBeforeAdvice advice;

    public MethodBeforeAdviceInterceptor() {
        
    }

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        advice.before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
        return methodInvocation.proceed();
    }
}
