package cn.element.core.bean.support;

import cn.element.core.BeansException;
import cn.element.core.bean.factory.BeanDefinition;

/**
 * 实例化Bean类
 *
 * 在 AbstractAutowireCapableBeanFactory 类中实现了 Bean 的实例化操作 newInstance，其实这块会埋下一个坑，
 * 有构造函数入参的对象怎么处理？可以提前思考
 * 在处理完 Bean 对象的实例化后，直接调用 addSingleton 方法存放到单例对象的缓存中去。
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {

        Object bean = null;

        try {
            bean = beanDefinition.getBeanClass().newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        this.addSingleton(beanName, bean);

        return bean;
    }
}
