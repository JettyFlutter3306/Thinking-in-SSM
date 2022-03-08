package cn.element.ioc.beans.factory.support;

import cn.element.ioc.beans.BeansException;

import cn.element.ioc.beans.factory.FactoryBean;
import cn.element.ioc.beans.factory.config.BeanDefinition;
import cn.element.ioc.beans.factory.config.BeanPostProcessor;
import cn.element.ioc.beans.factory.config.ConfigurableBeanFactory;
import cn.element.ioc.core.convert.ConversionService;
import cn.element.ioc.util.ClassUtils;
import cn.element.ioc.util.StringValueResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象类定义模板方法类
 *
 * AbstractBeanFactory 首先继承了 DefaultSingletonBeanRegistry，也就具备了使用单例注册类方法。
 * 接下来很重要的一点是关于接口 BeanFactory 的实现，在方法 getBean 的实现过程中可以看到，
 * 主要是对单例 Bean 对象的获取以及在获取不到时需要拿到 Bean 的定义做相应
 * Bean 实例化操作。那么 getBean 并没有自身的去实现这些方法，
 * 而是只定义了调用过程以及提供了抽象方法，由实现此抽象类的其他类做相应实现。
 * 后续继承抽象类 AbstractBeanFactory 的类有两个，
 * 包括：AbstractAutowireCapableBeanFactory、DefaultListableBeanFactory，
 *
 * 首先这里把 AbstractBeanFactory 原来继承的 DefaultSingletonBeanRegistry，修改
 * 为继承 FactoryBeanRegistrySupport。因为需要扩展出创建 FactoryBean 对象的能
 * 力，所以这就想一个链条服务上，截出一个段来处理额外的服务，并把链条再链接
 * 上。
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport
                                          implements ConfigurableBeanFactory {

    /**
     * ClassLoader to resolve bean class names with, if necessary
     */
    private final ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    /** BeanPostProcessors to apply in createBean */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    /**
     * String resolvers to apply e.g. to annotation attribute values
     */
    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

    private ConversionService conversionService;

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }

    protected <T> T doGetBean(final String name, final Object[] args) {
        Object sharedInstance = getSingleton(name);
        if (sharedInstance != null) {
            // 如果是FactoryBean,则需要调用FactoryBean getObject()
            return (T) getObjectForBeanInstance(sharedInstance, name);
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        Object bean = createBean(name, beanDefinition, args);
        return (T) getObjectForBeanInstance(bean, name);
    }

    private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }

        Object object = getCachedObjectsForFactoryBean(beanName);

        if (object == null) {
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            object = getObjectFromFactoryBean(factoryBean, beanName);
        }

        return object;
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }

    /**
     * Return the list of BeanPostProcessors that will get applied
     * to beans created with this factory.
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    public ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        embeddedValueResolvers.add(valueResolver);
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        
        for (StringValueResolver resolver : embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
        }
        
        return result;
    }

    @Override
    public ConversionService getConversionService() {
        return conversionService;
    }

    @Override
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
