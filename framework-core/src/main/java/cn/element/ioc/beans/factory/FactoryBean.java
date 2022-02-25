package cn.element.ioc.beans.factory;

/**
 * FactoryBean 中需要提供 3 个方法，获取对象、对象类型，以及是否是单例对象，
 * 如果是单例对象依然会被放到内存中
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();

    boolean isSingleton();

}
