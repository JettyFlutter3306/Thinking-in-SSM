package cn.element.ioc.test.demo.advice;

import cn.element.ioc.aop.MethodAroundAdvice;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class UserAroundAdvice implements MethodAroundAdvice {

    @Override
    public Object around(Method method, Object[] args, Object target) throws Throwable {
        long start = System.currentTimeMillis();
        log.debug("环绕通知前置计时开始...");
        
        Object o = method.invoke(target, args);

        long end = System.currentTimeMillis();
        log.debug("环绕通知后置计时开始...");
        log.info("本次调用方法用时: {} ms", end - start);
        return o;
    }
}
