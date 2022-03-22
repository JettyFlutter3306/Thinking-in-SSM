package cn.element.beta.framework.context;

import cn.element.beta.framework.aop.AopProxy;
import cn.element.beta.framework.aop.CglibAopProxy;
import cn.element.beta.framework.aop.JdkDynamicAopProxy;
import cn.element.beta.framework.aop.config.AopConfig;
import cn.element.beta.framework.aop.support.AdvisedSupport;
import cn.element.beta.framework.beans.BeanWrapper;
import cn.element.beta.framework.beans.config.BeanDefinition;
import cn.element.beta.framework.beans.config.BeanPostProcessor;
import cn.element.beta.framework.beans.support.BeanDefinitionReader;
import cn.element.beta.framework.beans.support.DefaultListableBeanFactory;
import cn.element.beta.framework.core.BeanFactory;
import cn.element.ioc.beans.factory.annotation.Autowired;
import cn.element.ioc.stereotype.Component;
import cn.element.ioc.stereotype.Controller;
import cn.element.ioc.stereotype.Repository;
import cn.element.ioc.stereotype.Service;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {

    private final String[] configLocations;

    private BeanDefinitionReader reader;

    /**
     * 单例的IoC容器缓存
     */
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    /**
     * 普通的IoC容器
     */
    private final Map<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    public ApplicationContext(String... configLocations) {
        this.configLocations = configLocations;

        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void refresh() throws Exception {
        // 1.定位配置文件
        reader = new BeanDefinitionReader(configLocations);

        // 2.加载配置文件,扫描相关的类,吧它们封装成BeanDefinition
        List<BeanDefinition> definitions = reader.loadBeanDefinitions();

        // 3.注册,把配置信息放到容器里面(伪IoC容器)
        doRegisterBeanDefinition(definitions);

        // 4.把不是延时加载的类提前初始化
        doAutowire();
    }

    /**
     * 依赖注入,从这里开始,读取BeanDefinition中的信息
     * 然后通过反射机制创建一个实例并返回
     * Spring源码中不会把原始的对象放进去,会用一个BeanWrapper来进行一次包装
     * 装饰器模式: 保留原来的OOP关系,需要对它进行扩展,增强
     */
    @Override
    public Object getBean(String beanName) {
        BeanDefinition definition = beanDefinitionMap.get(beanName);
        Object bean = null;

        try {
            // 生成通知事件
            BeanPostProcessor processor = new BeanPostProcessor();

            // 在实例初始化以前调用一次
            processor.postProcessBeforeInitialization(bean, beanName);

            bean = instantiateBean(beanName, definition);

            BeanWrapper beanWrapper = new BeanWrapper(bean);

            factoryBeanInstanceCache.put(beanName, beanWrapper);

            // 在实例化初始化之后调用一次
            processor.postProcessAfterInitialization(bean, beanName);

            populateBean(beanName, new BeanDefinition(), beanWrapper);

            return factoryBeanInstanceCache.get(beanName).getWrappedInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return reader.getConfig();
    }

    /**
     * 只处理非延时加载的情况
     */
    private void doAutowire() {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();

            if (!entry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) throws Exception {
        for (BeanDefinition definition : beanDefinitions) {
            if (beanDefinitionMap.containsKey(definition.getFactoryBeanName())) {
                throw new Exception("The '" + definition.getFactoryBeanName() + "' existed");
            }

            beanDefinitionMap.put(definition.getFactoryBeanName(), definition);
        }
    }

    private void populateBean(String beanName, BeanDefinition beanDefinition, BeanWrapper wrapper) {
        Object instance = wrapper.getWrappedInstance();
        Class<?> clazz = wrapper.getWrappedClass();

        // 判断只有加了注解的类，才执行依赖注入
        if (!(clazz.isAnnotationPresent(Controller.class) ||
                clazz.isAnnotationPresent(Service.class) ||
                clazz.isAnnotationPresent(Repository.class) ||
                clazz.isAnnotationPresent(Component.class))) {
            return;
        }

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Autowired autowired = field.getAnnotation(Autowired.class);
                String name = autowired.value();

                if (StrUtil.isBlank(name)) {
                    name = field.getType().getName();
                }

                field.setAccessible(true);

                try {
                    //为什么会为NULL，先留个坑
                    if (factoryBeanInstanceCache.get(name) == null) {
                        continue;
                    }
//                  if (instance == null) {
//                      continue;
//                  }
                    field.set(instance, factoryBeanInstanceCache.get(name).getWrappedInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 传一个BeanDefinition,就返回一个实例Bean
     */
    private Object instantiateBean(String beanName, BeanDefinition beanDefinition) {
        Object bean;
        String className = beanDefinition.getBeanClassName();

        try {
            // 根据Class才能确定一个类是否有实例
            if (factoryBeanObjectCache.containsKey(className)) {
                bean = factoryBeanObjectCache.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                bean = clazz.newInstance();

                AdvisedSupport config = instantiateAopConfig(beanDefinition);
                config.setTargetClass(clazz);
                config.setTarget(bean);

                // 符合PointCut的规则的话，闯将代理对象
                if(config.pointCutMatch()) {
                    bean = createProxy(config).getProxy();
                }

                factoryBeanObjectCache.put(className, bean);
                factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(), bean);
            }

            return bean;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    private AopProxy createProxy(AdvisedSupport config) {
        Class<?> targetClass = config.getTargetClass();

        if (targetClass.getInterfaces().length > 0) {
            return new JdkDynamicAopProxy(config);
        }

        return new CglibAopProxy(config);
    }

    private AdvisedSupport instantiateAopConfig(BeanDefinition BeanDefinition) {
        AopConfig config = new AopConfig();

        config.setPointCut(reader.getConfig().getProperty("pointCut"))
              .setAspectClass(reader.getConfig().getProperty("aspectClass"))
              .setAspectBefore(reader.getConfig().getProperty("aspectBefore"))
              .setAspectAfter(reader.getConfig().getProperty("aspectAfter"))
              .setAspectAfterThrow(reader.getConfig().getProperty("aspectAfterThrow"))
              .setAspectAfterThrowing(this.reader.getConfig().getProperty("aspectAfterThrowing"));

        return new AdvisedSupport(config);
    }


}
