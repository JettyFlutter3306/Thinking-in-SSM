package cn.element.ioc.beans.factory;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.PropertyValue;
import cn.element.ioc.beans.PropertyValues;
import cn.element.ioc.beans.factory.config.BeanDefinition;
import cn.element.ioc.beans.factory.config.BeanFactoryPostProcessor;
import cn.element.ioc.core.io.DefaultResourceLoader;
import cn.element.ioc.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

/**
 * Allows for configuration of individual bean property values from a property resource,
 * i.e. a properties file. Useful for custom config files targeted at system
 * administrators that override bean properties configured in the application context.
 * 
 * 依赖于 BeanFactoryPostProcessor 在 Bean 生命周期的属性，可以在 Bean 对象
 * 实例化之前，改变属性信息。所以这里通过实现 BeanFactoryPostProcessor 接
 * 口，完成对配置文件的加载以及摘取占位符中的在属性文件里的配置
 * 
 * 这样就可以把提取到的配置信息放置到属性配置中了
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    /**
     * Default placeholder prefix
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * Default placeholder suffix
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
    
    private String location;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 加载属性文件
        try {
            DefaultResourceLoader loader = new DefaultResourceLoader();
            Resource resource = loader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            String[] names = beanFactory.getBeanDefinitionNames();

            for (String name : names) {
                BeanDefinition definition = beanFactory.getBeanDefinition(name);
                PropertyValues propertyValues = definition.getPropertyValues();

                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    
                    if (!(value instanceof String)) {
                        continue;
                    }
                    
                    String strVal = (String) value;
                    StringBuilder sb = new StringBuilder(strVal);
                    
                    int startIndex = strVal.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
                    int stopIndex = strVal.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
                    
                    if (startIndex != -1 && stopIndex != -1 && startIndex < stopIndex) {
                        String propKey = strVal.substring(startIndex + 2, stopIndex);
                        String propVal = properties.getProperty(propKey);
                        
                        sb.replace(startIndex, stopIndex + 1, propVal);
                        propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), sb.toString()));
                    }
                }
            }
        } catch (IOException e) {
            throw new BeansException("Could not load properties", e);
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
