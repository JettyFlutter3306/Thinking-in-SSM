package cn.element.core.test.common;

import cn.element.core.beans.BeansException;
import cn.element.core.beans.PropertyValue;
import cn.element.core.beans.PropertyValues;
import cn.element.core.beans.factory.ConfigurableListableBeanFactory;
import cn.element.core.beans.factory.config.BeanDefinition;
import cn.element.core.beans.factory.config.BeanFactoryPostProcessor;

public class IBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");

        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        propertyValues.addPropertyValue(new PropertyValue("company", "改为：字节跳动 "));
    }
}
