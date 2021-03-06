package cn.element.ioc.beans.factory.support;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.PropertyValue;
import cn.element.ioc.beans.PropertyValues;
import cn.element.ioc.beans.factory.*;
import cn.element.ioc.beans.factory.config.*;
import cn.element.ioc.core.convert.ConversionService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/*
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
 *
 * 抽象类 AbstractAutowireCapableBeanFactory 中的 createBean 是用来创建 Bean
 * 对象的方法，在这个方法中我们之前已经扩展了 BeanFactoryPostProcessor、
 * BeanPostProcessor 操作，这里我们继续完善执行 Bean 对象的初始化方法的处理动作。
 *
 * 在方法 invokeInitMethods 中，主要分为两块来执行实现了 InitializingBean 接口
 * 的操作，处理 afterPropertiesSet 方法。另外一个是判断配置信息 init-method 是
 * 否存在，执行反射调用 initMethod.invoke(bean)。这两种方式都可以在 Bean 对象
 * 初始化过程中进行处理加载 Bean 对象中的初始化操作，让使用者可以额外新增加自己想要的动作。
 *
 * 在创建 Bean 对象的实例的时候，需要把销毁方法保存起来，方便后续执行销毁动
 * 作进行调用。
 *
 * 那么这个销毁方法的具体方法信息，会被注册到 DefaultSingletonBeanRegistry 中
 * 新增加的 Map<String, DisposableBean> disposableBeans 属性中
 * 去，因为这个接口的方法最终可以被类 AbstractApplicationContext 的 close 方法
 * 通过 getBeanFactory().destroySingletons() 调用。
 *
 * 在注册销毁方法的时候，会根据是接口类型和配置类型统一交给
 * DisposableBeanAdapter 销毁适配器类来做统一处理。实现了某个接口的类可以被
 * instanceof 判断或者强转后调用接口方法
 * 
 * AbstractAutowireCapableBeanFactory#createBean 方法中有这一条新增加的方法调
 * 用，就是在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性
 * 值 的操作applyBeanPostProcessorsBeforeApplyingPropertyValues
 * 
 * 那么这个 applyBeanPostProcessorsBeforeApplyingPropertyValues 方法中，首先就
 * 是获取已经注入的 BeanPostProcessor 集合并从中筛选出继承接口
 * InstantiationAwareBeanPostProcessor 的实现类
 * 
 * 那么这个 applyBeanPostProcessorsBeforeApplyingPropertyValues 方法中，首先就
 * 是获取已经注入的 BeanPostProcessor 集合并从中筛选出继承接口
 * InstantiationAwareBeanPostProcessor 的实现类
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
                                                         implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    /**
     * 在 AbstractAutowireCapableBeanFactory#createBean 方法中，其实关注点就在于
     * initializeBean -> applyBeanPostProcessorsAfterInitialization 这一块逻辑的调用，最
     * 终完成 AOP 代理对象的创建操作
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        // 判断是否返回代理 Bean 对象
        Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
        
        if (bean != null) {
            return bean;
        }

        return doCreateBean(beanName, beanDefinition, args);
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Object bean;
        
        try {
            // 实例化 Bean
            bean = createBeanInstance(beanDefinition, beanName, args);

            // 处理循环依赖，将实例化后的Bean对象提前放入缓存中暴露出来
            if (beanDefinition.isSingleton()) {
                Object finalBean = bean;
                addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, beanDefinition, finalBean));
            }

            // 实例化后判断
            boolean continueWithPropertyPopulation = applyBeanPostProcessorsAfterInstantiation(beanName, bean);
            if (!continueWithPropertyPopulation) {
                return bean;
            }
            
            // 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
            applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
            
            // 给 Bean 填充属性
            applyPropertyValues(beanName, bean, beanDefinition);
            
            // 执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        // 注册实现了 DisposableBean 接口的 Bean 对象
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        // 判断 SCOPE_SINGLETON、SCOPE_PROTOTYPE
        Object exposedObject = bean;
        if (beanDefinition.isSingleton()) {
            // 获取代理对象
            exposedObject = getSingleton(beanName);
            registerSingleton(beanName, exposedObject);
        }
        
        return exposedObject;
    }

    protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) {
        Object exposedObject = bean;
        
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                exposedObject = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).getEarlyBeanReference(exposedObject, beanName);
                
                if (exposedObject == null) {
                    return exposedObject;
                }
            }
        }

        return exposedObject;
    }

    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 非Singleton类型的Bean不执行销毁方法
        if (!beanDefinition.isSingleton()) {
            return;
        }

        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (bean instanceof Aware) {
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            } else if (bean instanceof BeanClassLoaderAware) {
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
            } else if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
        }

        // 1.执行BeanPostProcessor Before处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", e);
        }

        // 2.执行BeanPostProcessor After处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);

        return wrappedBean;
    }

    private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        // 1.实现接口InitializingBean
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }

        // 2.配置信息init-method (判断是为了避免二次执行销毁)
        String methodName = beanDefinition.getInitMethodName();

        if (StrUtil.isNotEmpty(methodName)) {
            Method method = beanDefinition.getBeanClass().getMethod(methodName);

            if (method == null) {
                throw new BeansException("Could not find an init method named '" + methodName + "' on bean with name '" + beanName + "'");
            }

            method.invoke(bean);
        }
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
        Constructor<?> constructorToUse = null;
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
            
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();

                if (value instanceof BeanReference) {
                    // A 依赖 B，获取 B 的实例化
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                } else {
                    // 类型转换
                    Class<?> sourceType = value.getClass();
                    Class<?> targetType = (Class<?>) TypeUtil.getFieldType(bean.getClass(), name);
                    ConversionService conversionService = getConversionService();
                    
                    if (conversionService != null) {
                        if (conversionService.canConvert(sourceType, targetType)) {
                            value = conversionService.convert(value, targetType);
                        }
                    }
                }

                // 反射设置属性填充
                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values：" + beanName);
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
    
    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        
        if (null != bean) {
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        
        return bean;
    }
    
    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
                
                if (result != null) {
                    return result;
                }
            }
        }
        
        return null;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;

        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);

            if (current == null) {
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

            if (current == null) {
                return result;
            }

            result = current;
        }

        return result;
    }

    /**
     * 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
     */
    protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                
                if (null != pvs) {
                    for (PropertyValue propertyValue : pvs.getPropertyValues()) {
                        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                    }
                }
            }
        }
    }

    /**
     * Bean 实例化后对于返回 false 的对象，不在执行后续设置 Bean 对象属性的操作
     */
    private boolean applyBeanPostProcessorsAfterInstantiation(String beanName, Object bean) {
        boolean continueWithPropertyPopulation = true;
        
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor = (InstantiationAwareBeanPostProcessor) beanPostProcessor;
                
                if (!instantiationAwareBeanPostProcessor.postProcessAfterInstantiation(bean, beanName)) {
                    continueWithPropertyPopulation = false;
                    break;
                }
            }
        }
        
        return continueWithPropertyPopulation;
    }
}
