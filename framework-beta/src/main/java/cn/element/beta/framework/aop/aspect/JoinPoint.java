package cn.element.beta.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * 回调连接点,通过它可以获得被代理的业务方法的所有信息
 */
public interface JoinPoint {

    /**
     * 业务方法本身
     */
    Method getMethod();

    /**
     * 该方法的实参列表
     */
    Object[] getArguments();

    /**
     * 该方法所属的实例对象
     */
    Object getThis();

    /**
     * 在JoinPoint中添加自定义属性
     */
    void setUserAttribute(String key, Object value);

    /**
     * 从已添加的自定义属性中获取一个属性值
     */
    Object getUserAttribute(String key);
}
