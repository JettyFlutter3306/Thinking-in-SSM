package cn.element.ioc.beans.factory;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.factory.config.*;


public interface ConfigurableListableBeanFactory extends ListableBeanFactory,
                                                         AutowireCapableBeanFactory,
                                                         ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
