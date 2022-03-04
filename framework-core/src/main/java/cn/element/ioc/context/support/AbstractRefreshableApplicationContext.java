package cn.element.ioc.context.support;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.factory.ConfigurableListableBeanFactory;
import cn.element.ioc.beans.factory.support.DefaultListableBeanFactory;

/**
 * 获取 Bean 工厂和加载资源
 * 在 refreshBeanFactory() 中主要是获取了 DefaultListableBeanFactory
 * 的实例化以及对资源配置的加载操作
 * loadBeanDefinitions(beanFactory)，在加载完成后即可完成对
 * application16.xml 配置文件中 Bean 对象的定义和注册，同时也包括实现了接口
 * BeanFactoryPostProcessor、BeanPostProcessor 的配置 Bean 信息。
 *
 * 但此时资源加载还只是定义了一个抽象类方法
 * loadBeanDefinitions(DefaultListableBeanFactory beanFactory)，继续由其他抽象类继承实现
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;
    
    @Override
    protected void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    protected DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }


}