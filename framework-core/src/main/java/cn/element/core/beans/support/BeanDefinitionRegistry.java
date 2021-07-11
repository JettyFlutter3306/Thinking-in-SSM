package cn.element.core.beans.support;

import cn.element.core.beans.factory.BeanDefinition;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);


}
