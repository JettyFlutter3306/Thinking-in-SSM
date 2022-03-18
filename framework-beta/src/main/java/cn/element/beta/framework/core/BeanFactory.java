package cn.element.beta.framework.core;

/**
 * 单例工厂的顶层设计
 */
public interface BeanFactory {

    /**
     * 根据beanName从IoC容器中获得一个实例Bean
     */
    Object getBean(String beanName) throws Exception;
    
    Object getBean(Class<?> beanClass) throws Exception;
    
}
