package cn.element.core.beans.factory;

/**
 * BeanDefinition是Spring框架里面一个常用的类
 * 例如它会包括singleton,prototype,BeanClassName等
 * 目前我们初步实现会更加简单的处理
 */
public class BeanDefinition {

    private Class bean;

    public BeanDefinition(Class bean) {

        this.bean = bean;
    }

    public Class getBeanClass() {

        return bean;
    }
}
