package cn.element.core.bean.support;

import cn.element.core.bean.factory.BeanDefinition;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);


}
