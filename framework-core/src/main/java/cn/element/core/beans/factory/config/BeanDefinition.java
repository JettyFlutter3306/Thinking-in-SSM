package cn.element.core.beans.factory.config;

import cn.element.core.beans.PropertyValues;

/**
 * BeanDefinition是Spring框架里面一个常用的类
 * 例如它会包括singleton,prototype,BeanClassName等
 * 目前我们初步实现会更加简单的处理
 *
 * 在Bean注册的过程中需要传递Bean的信息
 * 所以为了把属性一定交给Bean定义,所以这里填充了PropertyValues属性,同时
 * 把两个构造函数做了一些简单的优化,避免后面for循环还得判断属性填充是否为空
 */
public class BeanDefinition {

    private Class beanClass;

    private PropertyValues propertyValues;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }
}
