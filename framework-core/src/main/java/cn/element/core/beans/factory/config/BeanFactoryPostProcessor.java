package cn.element.core.beans.factory.config;

import cn.element.core.beans.BeansException;
import cn.element.core.beans.factory.ConfigurableListableBeanFactory;

public interface BeanFactoryPostProcessor {

    /**
     * 在所有的BeanDefinition加载完成之后,实例化Bean对象之前,提供修改
     * BeanDefinition属性的机制
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;


}
