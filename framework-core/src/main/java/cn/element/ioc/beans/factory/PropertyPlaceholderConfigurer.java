package cn.element.ioc.beans.factory;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.PropertyValue;
import cn.element.ioc.beans.PropertyValues;
import cn.element.ioc.beans.factory.config.BeanDefinition;
import cn.element.ioc.beans.factory.config.BeanFactoryPostProcessor;
import cn.element.ioc.core.io.DefaultResourceLoader;
import cn.element.ioc.core.io.Resource;
import cn.element.ioc.util.StringValueResolver;

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

    /**
     * 在解析属性配置的类 PropertyPlaceholderConfigurer 中，最主要的其实就是这行
     * 代码的操作
     * beanFactory.addEmbeddedValueResolver(valueResolver) 这是把
     * 属性值写入到了 AbstractBeanFactory 的 embeddedValueResolvers 中
     * 
     * 这里说明下，embeddedValueResolvers 是 AbstractBeanFactory 类新增加的集合
     * List<StringValueResolver> embeddedValueResolvers String 
     * resolvers to apply e.g. to annotation attribute values
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            // 加载属性文件
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);

            // 占位符替换属性值
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanName : beanDefinitionNames) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

                PropertyValues propertyValues = beanDefinition.getPropertyValues();
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    
                    if (!(value instanceof String)) {
                        continue;
                    }
                    
                    value = resolvePlaceholder((String) value, properties);
                    propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), value));
                }
            }

            // 向容器中添加字符串解析器，供解析@Value注解使用
            StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
            beanFactory.addEmbeddedValueResolver(valueResolver);
        } catch (IOException e) {
            throw new BeansException("Could not load properties", e);
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String resolvePlaceholder(String value, Properties properties) {
        StringBuilder buffer = new StringBuilder(value);
        
        int startIdx = value.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int stopIdx = value.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
        
        if (startIdx != -1 && stopIdx != -1 && startIdx < stopIdx) {
            String propKey = value.substring(startIdx + 2, stopIdx);
            String propVal = properties.getProperty(propKey);
            
            buffer.replace(startIdx, stopIdx + 1, propVal);
        }
        
        return buffer.toString();
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolveStringValue(String strVal) {
            return PropertyPlaceholderConfigurer.this.resolvePlaceholder(strVal, properties);
        }

    }
}
