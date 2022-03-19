package cn.element.beta.framework.beans.config;

/**
 * BeanPostProcessor是为对象初始化事件设置的一种回调机制
 */
public class BeanPostProcessor {
    
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
    
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

}
