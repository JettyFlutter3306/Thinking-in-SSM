package cn.element.core.beans.factory.support;

import cn.element.core.beans.BeansException;
import cn.element.core.beans.factory.config.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultListableBeanFactory在Spring源码中也是一个非常核心的类
 * 它继承了AbstractAutowireCapableBeanFactory类,也就具备了接口
 * BeanFactory和AbstractBeanFactory等一连串的功能实现,所以有时候会看到
 * 一些类的强制转换,调用某些方法,也是因为强转的类型实现接口或继承了某些类
 *
 * 除此之外这个类还实现了接口BeanDefinitionRegistry中registerBeanDefinition(beanName, beanDefinition)方法
 * 当然还会看到一个getBeanDefinition的实现,这个方法之前提到过它是抽象类AbstractBeanFactory中定义的抽象方法
 * 现在注册Bean定义与获取Bean定义就可以同时使用了
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        if (beanDefinition == null) {
            throw new BeansException("No bean named '" + beanName + "' is defined!");
        }

        return beanDefinition;
    }
}
