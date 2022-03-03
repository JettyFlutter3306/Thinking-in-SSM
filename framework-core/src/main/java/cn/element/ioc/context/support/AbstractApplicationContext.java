package cn.element.ioc.context.support;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.factory.ConfigurableListableBeanFactory;
import cn.element.ioc.beans.factory.config.BeanFactoryPostProcessor;
import cn.element.ioc.beans.factory.config.BeanPostProcessor;
import cn.element.ioc.context.ApplicationEvent;
import cn.element.ioc.context.ApplicationListener;
import cn.element.ioc.context.ConfigurableApplicationContext;
import cn.element.ioc.context.event.ApplicationEventMulticaster;
import cn.element.ioc.context.event.ContextClosedEvent;
import cn.element.ioc.context.event.ContextRefreshedEvent;
import cn.element.ioc.context.event.SimpleApplicationEventMulticaster;
import cn.element.ioc.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

/**
 * 在抽象应用上下文 AbstractApplicationContext#refresh 中，主要新增了 初始化
 * 事件发布者、注册事件监听器、发布容器刷新完成事件，三个方法用于处
 * 理事件操作。
 *
 * 初始化事件发布者(initApplicationEventMulticaster)，主要用于实例化一个
 * SimpleApplicationEventMulticaster，这是一个事件广播器。
 *
 * 注册事件监听器(registerListeners)，通过 getBeansOfType 方法获取到所有从
 * spring.xml 中加载到的事件配置 Bean 对象。
 *
 * 发布容器刷新完成事件(finishRefresh)，发布了第一个服务器启动完成后的事件，这
 * 个事件通过 publishEvent 发布出去，其实也就是调用了
 * applicationEventMulticaster.multicastEvent(event); 方法。
 *
 * 最后是一个 close 方法中，新增加了发布一个容器关闭事件。
 * publishEvent(new ContextClosedEvent(this));
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader
                                                 implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    protected abstract void refreshBeanFactory() throws BeansException;

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

        // 6.初始化事件发布者
        initApplicationEventMulticaster();

        // 7.注册事件监听器
        registerListeners();

        // 8.提前实例化单例Bean对象
        beanFactory.preInstantiateSingletons();

        // 9.发布容器刷新完成事件
        finishRefresh();
    }

    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);

        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    private void registerListeners() {
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();

        for (ApplicationListener listener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }

    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

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
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
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
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        // 发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));

        // 执行销毁单例bean的销毁方法
        getBeanFactory().destroySingletons();
    }
}
