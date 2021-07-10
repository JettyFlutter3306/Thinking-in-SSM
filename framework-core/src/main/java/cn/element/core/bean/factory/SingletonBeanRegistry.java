package cn.element.core.bean.factory;

/**
 * 单例注册接口定义和实现
 */
public interface SingletonBeanRegistry {

    Object getSingleton(String beanName);
}
