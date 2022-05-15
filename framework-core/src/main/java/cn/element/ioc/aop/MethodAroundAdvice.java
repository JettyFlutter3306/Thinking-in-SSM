package cn.element.ioc.aop;

import java.lang.reflect.Method;

public interface MethodAroundAdvice extends AroundAdvice {
    
    Object around(Method method, Object[] args, Object target) throws Throwable;

}
