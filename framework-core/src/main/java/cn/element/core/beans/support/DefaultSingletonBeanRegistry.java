package cn.element.core.beans.support;

import cn.element.core.beans.factory.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这个类比较简单主要是定义了一个获取单例对象的接口。
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private Map<String,Object> singletonObjects = new ConcurrentHashMap<>();

    @Override
    public Object getSingleton(String beanName) {

        return singletonObjects.get(beanName);
    }

    protected void addSingleton(String beanName, Object singleObject) {

        singletonObjects.put(beanName, singleObject);
    }
}
