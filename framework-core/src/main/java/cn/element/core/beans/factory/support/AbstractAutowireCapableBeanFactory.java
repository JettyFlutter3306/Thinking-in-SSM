package cn.element.core.beans.factory.support;

import cn.element.core.beans.BeansException;
import cn.element.core.beans.PropertyValue;
import cn.element.core.beans.PropertyValues;
import cn.element.core.beans.factory.config.AutowireCapableBeanFactory;
import cn.element.core.beans.factory.config.BeanDefinition;
import cn.element.core.beans.factory.config.BeanPostProcessor;
import cn.element.core.beans.factory.config.BeanReference;
import cn.hutool.core.bean.BeanUtil;

import java.lang.reflect.Constructor;

/**
 * 实例化Bean类
 *
 * 在 AbstractAutowireCapableBeanFactory 类中实现了 Bean 的实例化操作 newInstance，其实这块会埋下一个坑，
 * 有构造函数入参的对象怎么处理？可以提前思考
 * 在处理完 Bean 对象的实例化后，直接调用 addSingleton 方法存放到单例对象的缓存中去。
 *
 * 实现 BeanPostProcessor 接口后，会涉及到两个接口方法，
 * postProcessBeforeInitialization、
 * postProcessAfterInitialization，分别作用于 Bean 对象执行初始化前
 * 后的额外处理。
 *
 * 除此之外这个类还实现了接口BeanDefinitionRegistry中registerBeanDefinition(beanName, beanDefinition)方法
 * 当然还会看到一个getBeanDefinition的实现,这个方法之前提到过它是抽象类AbstractBeanFactory中定义的抽象方法
 * 现在注册Bean定义与获取Bean定义就可以同时使用了
 *
 * 也就是需要在创建 Bean 对象时，在 createBean 方法中添加 initializeBean(beanName, bean, beanDefinition); 操作。而这个
 * 操作主要主要是对于方法
 * applyBeanPostProcessorsBeforeInitialization、
 * applyBeanPostProcessorsAfterInitialization 的使用。
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean;

        try {
            bean = createBeanInstance(beanDefinition, beanName, args);

            // 给Bean填充属性
            applyPropertyValues(beanName, bean, beanDefinition);

            // 执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        addSingleton(beanName, bean);
        return bean;
    }

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 1.执行BeanPostProcessor Before处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        // 待完成内容: invokeInitMethods(beanName, wrappedBean, beanDefinition)
        invokeInitMethods(beanName, wrappedBean, beanDefinition);

        // 2.执行BeanPostProcessor After处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);

        return wrappedBean;
    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {

    }

    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) throws BeansException {
        Constructor constructorToUse = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] constructors = beanClass.getDeclaredConstructors();

        for (Constructor<?> constructor : constructors) {
            if (args != null && constructor.getParameterTypes().length == args.length) {
                constructorToUse = constructor;
                break;
            }
        }

        return getInstantiationStrategy().instantiate(beanDefinition, beanName, constructorToUse, args);
    }

    /**
     * Bean属性填充
     * 在 applyPropertyValues 中，通过获取
     * beanDefinition.getPropertyValues() 循环进行属性填充操作，如果遇
     * 到的是 BeanReference，那么就需要递归获取 Bean 实例，调用 getBean 方法。
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();

            for (PropertyValue pv : propertyValues.getPropertyValues()) {
                String name = pv.getName();
                Object value = pv.getValue();

                if (value instanceof BeanReference) {
                    // A依赖B, 获取B的变化
                    BeanReference reference = (BeanReference) value;
                    value = getBean(reference.getBeanName());
                }

                BeanUtil.setFieldValue(bean, name, value);  // 属性填充
            }
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;

        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);

            if (null == current) {
                return result;
            }

            result = current;
        }

        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;

        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);

            if (null == current) {
                return result;
            }

            result = current;
        }

        return result;
    }
}
