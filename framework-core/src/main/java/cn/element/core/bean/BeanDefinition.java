package cn.element.core.bean;

/**
 * BeanDefinition是Spring框架里面一个常用的类
 * 例如它会包括singleton,prototype,BeanClassName等
 * 目前我们初步实现会更加简单的处理
 */
public class BeanDefinition {

    private Object bean;

    public BeanDefinition(Object bean) {

        this.bean = bean;
    }

    public Object getBean() {

        return bean;
    }
}
