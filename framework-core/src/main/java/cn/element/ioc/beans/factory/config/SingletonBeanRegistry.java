package cn.element.ioc.beans.factory.config;

/**
 * 单例注册接口定义和实现
 * 这个类比较简单主要是定义了一个获取单例对象的接口
 */
public interface SingletonBeanRegistry {

    Object getSingleton(String beanName);
}
