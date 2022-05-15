package cn.element.ioc.test.demo;

import cn.element.ioc.aop.MethodAroundAdvice;

import java.lang.reflect.Method;

public class UserAroundAdvice implements MethodAroundAdvice {

    @Override
    public Object around(Method method, Object[] args, Object target) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("环绕通知前置计时开始...");

        Thread.sleep(1000);
        Object o = method.invoke(target, args);

        long end = System.currentTimeMillis();
        System.out.println("环绕通知后置计时开始...");
        System.out.println("本次调用方法用时: " + (end - start) + " ms");
        return o;
    }
}
