package cn.element.ioc.beans.factory.annotation;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.PropertyValues;
import cn.element.ioc.beans.factory.BeanFactory;
import cn.element.ioc.beans.factory.BeanFactoryAware;
import cn.element.ioc.beans.factory.ConfigurableListableBeanFactory;
import cn.element.ioc.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.element.ioc.util.ClassUtils;
import cn.hutool.core.bean.BeanUtil;

import java.lang.reflect.Field;

/**
 * AutowiredAnnotationBeanPostProcessor 是实现接口
 * InstantiationAwareBeanPostProcessor 的一个用于在 Bean 对象实例化完成后，设
 * 置属性操作前的处理属性信息的类和操作方法。只有实现了 BeanPostProcessor 
 * 接口才有机会在 Bean 的生命周期中处理初始化信息
 * 
 * 核心方法 postProcessPropertyValues，主要用于处理类含有 @Value、@Autowired 
 * 注解的属性，进行属性信息的提取和设置
 * 
 * 这里需要注意一点因为我们在 AbstractAutowireCapableBeanFactory 类中使用的
 * 是 CglibSubclassingInstantiationStrategy 进行类的创建，所以在
 * AutowiredAnnotationBeanPostProcessor#postProcessPropertyValues 中需要判断是
 * 否为 CGlib 创建对象，否则是不能正确拿到类信息的。
 * ClassUtils.isCglibProxyClass(clazz) ? 
 * clazz.getSuperclass() : clazz;
 */
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor,
                                                             BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        // 1. 处理注解 @Value
        Class<?> clazz = bean.getClass();
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;

        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field field : declaredFields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            
            if (valueAnnotation != null) {
                String value = valueAnnotation.value();
                value = beanFactory.resolveEmbeddedValue(value);
                BeanUtil.setFieldValue(bean, field.getName(), value);
            }
        }

        // 2. 处理注解 @Autowired
        for (Field field : declaredFields) {
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            
            if (autowiredAnnotation != null) {
                Class<?> fieldType = field.getType();
                String dependentBeanName;
                Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
                Object dependentBean;
                
                if (null != qualifierAnnotation) {
                    dependentBeanName = qualifierAnnotation.value();
                    dependentBean = beanFactory.getBean(dependentBeanName, fieldType);
                } else {
                    dependentBean = beanFactory.getBean(fieldType);
                }
                
                BeanUtil.setFieldValue(bean, field.getName(), dependentBean);
            }
        }

        return pvs;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
