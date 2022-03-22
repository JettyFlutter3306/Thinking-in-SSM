package cn.element.beta.framework.aop.interceptor;

import cn.element.beta.framework.aop.aspect.JoinPoint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行拦截器链,相当于Spring中ReflectiveMethodInvocation的功能
 */
public class MethodInvocation implements JoinPoint {

    /**
     * 代理对象
     */
    private Object proxy;

    /**
     * 代理的目标方法
     */
    private Method method;

    /**
     * 代理的目标对象
     */
    private Object target;

    /**
     * 代理的目标类
     */
    private Class<?> targetClass;

    /**
     * 代理的方法的实参列表
     */
    private Object[] args;

    /**
     * 回调函数
     */
    private List<Object> matchers;

    /**
     * 保存自定义属性
     */
    private Map<String, Object> userAttributes;
    
    private int currentInterceptorIndex = -1;

    public MethodInvocation(Object proxy, Object target, Method method, Object[] args, Class<?> targetClass, List<Object> matchers) {
        this.proxy = proxy;
        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        this.args = args;
        this.matchers = matchers;
    }

    public Object proceed() throws Throwable {
        // 如果interceptor执行完了,则执行joinPoint
        if (currentInterceptorIndex == matchers.size() - 1) {
            return method.invoke(target, args);
        }
        
        Object advice = matchers.get(++currentInterceptorIndex);
        
        // 如果要动态匹配joinPoint
        if (advice instanceof MethodInterceptor) {
            MethodInterceptor interceptor = (MethodInterceptor) advice;
            return interceptor.invoke(this);
        } else {
            return proceed();
        }
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArguments() {
        return args;
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public void setUserAttribute(String key, Object value) {
        if (value != null) {
            if (userAttributes == null) {
                userAttributes = new HashMap<>();
            }
            
            userAttributes.put(key, value);
        } else {
            if (userAttributes != null) {
                userAttributes.remove(key);
            }
        }
    }

    @Override
    public Object getUserAttribute(String key) {
        return userAttributes != null ? userAttributes.get(key) : null;
    }
}
