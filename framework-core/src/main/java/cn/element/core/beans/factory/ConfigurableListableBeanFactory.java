package cn.element.core.beans.factory;

import cn.element.core.beans.BeansException;
import cn.element.core.beans.factory.config.*;


public interface ConfigurableListableBeanFactory extends ListableBeanFactory,
                                                         AutowireCapableBeanFactory,
                                                         ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
