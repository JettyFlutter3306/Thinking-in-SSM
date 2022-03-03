package cn.element.ioc.context.annotation;

import cn.element.ioc.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import cn.element.ioc.beans.factory.config.BeanDefinition;
import cn.element.ioc.beans.factory.support.BeanDefinitionRegistry;
import cn.element.ioc.stereotype.Component;
import cn.element.ioc.stereotype.Controller;
import cn.element.ioc.stereotype.Repository;
import cn.element.ioc.stereotype.Service;
import cn.hutool.core.util.StrUtil;

import java.util.Set;

/**
 * ClassPathBeanDefinitionScanner 是继承自
 * ClassPathScanningCandidateComponentProvider 的具体扫描包处理的类，在
 * doScan 中除了获取到扫描的类信息以后，还需要获取 Bean 的作用域和类名，如
 * 果不配置类名基本都是把首字母缩写
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {
    
    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }
    
    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> set = findCandidateComponents(basePackage);

            for (BeanDefinition beanDefinition : set) {
                // 解析Bean的作用域singleton,prototype
                String beanScope = resolveBeanScope(beanDefinition);
                
                if (StrUtil.isNotEmpty(beanScope)) {
                    beanDefinition.setScope(beanScope);
                }
                
                registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }
        }

        // 注册处理注解的 BeanPostProcessor（@Autowired、@Value）
        registry.registerBeanDefinition(
                "cn.element.ioc.context.annotation.internalAutowiredAnnotationProcessor", 
                new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class)
        );
    }

    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        
        if (scope != null) {
            return scope.value();
        }
        
        return StrUtil.EMPTY;
    }

    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        String value = "";
        
        if (beanClass.isAnnotationPresent(Component.class)) {
            Component component = beanClass.getAnnotation(Component.class);
            value = component.value();
        } else if (beanClass.isAnnotationPresent(Service.class)) {
            Service service = beanClass.getAnnotation(Service.class);
            value = service.value();
        } else if (beanClass.isAnnotationPresent(Repository.class)) {
            Repository repository = beanClass.getAnnotation(Repository.class);
            value = repository.value();
        } else if (beanClass.isAnnotationPresent(Controller.class)) {
            Controller controller = beanClass.getAnnotation(Controller.class);
            value = controller.value();
        }
        
        if (StrUtil.isEmpty(value)) {
            value = StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        
        return value;
    }
}
