package cn.element.core.beans.factory.support;

import cn.element.core.beans.factory.config.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在DefaultSingletonBeanRegistry中主要实现getSingleton()方法,同时实现一个受保护
 * 的addSingleton()方法,这个方法可以被继承此类的其他类调用,包括AbstractBeanFactory
 * 以及继承的DefaultListableBeanFactory调用
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String,Object> singletonObjects = new ConcurrentHashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    protected void addSingleton(String beanName, Object singleObject) {
        singletonObjects.put(beanName, singleObject);
    }
}
