package cn.element.core.beans.support;

import cn.element.core.BeansException;
import cn.element.core.beans.factory.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 实例化Bean类
 *
 * 在 AbstractAutowireCapableBeanFactory 类中实现了 Bean 的实例化操作 newInstance，其实这块会埋下一个坑，
 * 有构造函数入参的对象怎么处理？可以提前思考
 * 在处理完 Bean 对象的实例化后，直接调用 addSingleton 方法存放到单例对象的缓存中去。
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {

        Object bean = null;

        try {
            bean = this.createBeanInstance(beanDefinition, beanName, args);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        this.addSingleton(beanName, bean);

        return bean;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) throws BeansException {

        Constructor constructorToUse = null;

        Class<?> beanClass = beanDefinition.getBeanClass();

        Constructor<?>[] constructors = beanClass.getDeclaredConstructors();

        for (Constructor<?> constructor : constructors) {
            if(args != null && constructor.getParameterTypes().length == args.length){
                constructorToUse = constructor;

                break;
            }
        }

        return getInstantiationStrategy().instantiate(beanDefinition, beanName, constructorToUse, args);

    }

    public InstantiationStrategy getInstantiationStrategy() {

        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {

        this.instantiationStrategy = instantiationStrategy;
    }
}
