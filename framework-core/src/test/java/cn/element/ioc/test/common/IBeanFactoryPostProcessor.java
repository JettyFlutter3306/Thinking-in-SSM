package cn.element.ioc.test.common;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.PropertyValue;
import cn.element.ioc.beans.PropertyValues;
import cn.element.ioc.beans.factory.ConfigurableListableBeanFactory;
import cn.element.ioc.beans.factory.config.BeanDefinition;
import cn.element.ioc.beans.factory.config.BeanFactoryPostProcessor;

public class IBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");

        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        propertyValues.addPropertyValue(new PropertyValue("company", "改为：字节跳动 "));
    }
}
