package cn.element.beta.framework.aop.aspect;

import java.lang.reflect.Method;

public abstract class AbstractAspectJAdvice implements Advice {

    private final Method aspectMethod;
    
    private final Object aspectTarget;

    public AbstractAspectJAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    public Object invokeAdviceMethod(JoinPoint joinPoint, Object returnValue, Throwable tx) throws Throwable {
        Class<?>[] paramTypes = aspectMethod.getParameterTypes();

        if (paramTypes == null || paramTypes.length == 0) {
            return aspectMethod.invoke(aspectTarget);
        } else {
            Object[] args = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i] == JoinPoint.class) {
                    args[i] = joinPoint;
                } else if (paramTypes[i] == Throwable.class) {
                    args[i] = tx;
                } else if (paramTypes[i] == Object.class) {
                    args[i] = returnValue;
                }
            }
            
            return aspectMethod.invoke(aspectTarget, args);
        }
    }


}
