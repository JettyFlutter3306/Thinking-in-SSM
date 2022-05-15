package cn.element.ioc.test.demo;

import cn.element.ioc.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

public class UserBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("拦截对象: " + target);
        System.out.println("拦截方法：" + method.getName());
        System.out.println("参数: " + Arrays.toString(args));
    }
}
