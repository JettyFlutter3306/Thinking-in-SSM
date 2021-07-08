package cn.element.core.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 声明Bean工厂类
 * Bean工厂实现了注册Bean信息,同时在这个类中包括了获取Bean的操作
 */
public class BeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();  //保证线程安全,使用ConcurrentHashMap

    public Object getBeans(String name) {

        return beanDefinitionMap.get(name).getBean();
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {

        beanDefinitionMap.put(name, beanDefinition);
    }
}
