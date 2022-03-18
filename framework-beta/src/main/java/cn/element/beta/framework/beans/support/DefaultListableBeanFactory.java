package cn.element.beta.framework.beans.support;

import cn.element.beta.framework.beans.config.BeanDefinition;
import cn.element.beta.framework.context.support.AbstractApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * IoC容器的默认实现
 */
public class DefaultListableBeanFactory extends AbstractApplicationContext {

    protected final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    
    
}
