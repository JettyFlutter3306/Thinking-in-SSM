package cn.element.ioc.beans.factory;

import cn.element.ioc.beans.BeansException;

/**
 * Defines a factory which can return an Object instance
 * (possibly shared or independent) when invoked.
 */
public interface ObjectFactory<T> {
    
    T getObject() throws BeansException;

}
