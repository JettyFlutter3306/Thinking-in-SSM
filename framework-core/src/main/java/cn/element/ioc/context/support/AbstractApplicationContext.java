package cn.element.ioc.context.support;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.factory.ConfigurableListableBeanFactory;
import cn.element.ioc.beans.factory.config.BeanFactoryPostProcessor;
import cn.element.ioc.beans.factory.config.BeanPostProcessor;
import cn.element.ioc.context.ConfigurableApplicationContext;
import cn.element.ioc.core.io.DefaultResourceLoader;

import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader
                                                 implements ConfigurableApplicationContext {

    /**
     * refresh() 方法就是整个 Spring 容器的操作过程
     * 添加 ApplicationContextAwareProcessor，让继承自 ApplicationContextAware 的
     * Bean 对象都能感知所属的 ApplicationContext
     */
    @Override
    public void refresh() throws BeansException {
        // 1.创建BeanFactory,并加载BeanDefinition
        refreshBeanFactory();

        // 2.获取BeanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 3.添加ApplicationContextAwareProcessor,让继承自ApplicationContextAware的Bean对象都能感知到所属的ApplicationContext
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        // 4.在Bean实例化之前,执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        // 5.BeanPostProcessor需要提前于其他的Bean对象实例化之前执行注册操作
        registerBeanPostProcessors(beanFactory);

        // 6.提前实例化单例Bean对象
        beanFactory.preInstantiateSingletons();
    }

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    protected abstract void refreshBeanFactory() throws BeansException;

    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, BeanFactoryPostProcessor> map = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);

        for (BeanFactoryPostProcessor beanFactoryPostProcessor : map.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, BeanPostProcessor> map = beanFactory.getBeansOfType(BeanPostProcessor.class);

        for (BeanPostProcessor processor : map.values()) {
            beanFactory.addBeanPostProcessor(processor);
        }
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        getBeanFactory().destroySingletons();
    }
}
