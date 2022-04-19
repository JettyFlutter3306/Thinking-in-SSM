package cn.element.spring.calculator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class CalculatorLoggingHandler implements InvocationHandler {

    private final Object target;

    public CalculatorLoggingHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("方法名：" + method.getName() + "() 参数列表：" + Arrays.toString(args));
        Object result = method.invoke(target, args);
        System.out.println("方法名：" + method.getName() + "() 运行结果是：" + result);
        return result;
    }

    //创建代理对象。
    public static Object createProxy(Object target) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(), 
                target.getClass().getInterfaces(), 
                new CalculatorLoggingHandler(target)
        );
    }


}
