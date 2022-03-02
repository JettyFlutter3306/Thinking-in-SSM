package cn.element.ioc.context.annotation;

import cn.element.ioc.beans.factory.config.BeanDefinition;
import cn.element.ioc.stereotype.Component;
import cn.element.ioc.stereotype.Controller;
import cn.element.ioc.stereotype.Repository;
import cn.element.ioc.stereotype.Service;
import cn.hutool.core.util.ClassUtil;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 这里先要提供一个可以通过配置路径
 * basePackage=xxx，解析出
 * classes 信息的工具方法 findCandidateComponents，通过这个方法就可以扫描到
 * 所有 @Component 注解的 Bean 对象了
 */
public class ClassPathScanningCandidateComponentProvider {
    
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        Set<Class<?>> classes = ClassUtil.scanPackage(basePackage, clazz -> 
                clazz.isAnnotationPresent(Controller.class)
                || clazz.isAnnotationPresent(Service.class)
                || clazz.isAnnotationPresent(Repository.class)
                || clazz.isAnnotationPresent(Component.class)
        );

        for (Class<?> clazz : classes) {
            candidates.add(new BeanDefinition(clazz));
        }
        
        return candidates;
    }
}
