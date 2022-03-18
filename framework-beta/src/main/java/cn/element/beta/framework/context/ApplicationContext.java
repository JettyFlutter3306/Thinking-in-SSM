package cn.element.beta.framework.context;

import cn.element.beta.framework.beans.BeanWrapper;
import cn.element.beta.framework.beans.config.BeanDefinition;
import cn.element.beta.framework.beans.support.BeanDefinitionReader;
import cn.element.beta.framework.beans.support.DefaultListableBeanFactory;
import cn.element.beta.framework.core.BeanFactory;

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
    public Object getBean(String beanName) throws Exception {
        return null;
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
    
    
}
