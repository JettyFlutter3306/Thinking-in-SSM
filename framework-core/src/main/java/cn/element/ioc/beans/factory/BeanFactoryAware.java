package cn.element.ioc.beans.factory;

import cn.element.ioc.beans.BeansException;

/**
 * 容器感知接口
 *
 * 实现此接口，既能感知到所属的 BeanFactory
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;

}
