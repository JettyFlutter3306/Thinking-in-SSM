package cn.element.spring.factory;

import cn.element.spring.pojo.Employee;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 后置处理器
 * 
 * 特点: 
 * 1. 后置处理器 会对IOC容器 中的每个bean都起作用.
 */
public class IBeanPostProcessor implements BeanPostProcessor {

    /**
     * 在初始化方法之前执行
     * Object bean :  当前正在被创建的Bean对象.
     * String beanName :  当前正在被创建的Bean对象的id值. 
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 对bean对象进行初始化前置处理
        System.out.println("postProcessBeforeInitialization==> "+ bean + " , " + beanName );
        Employee employee  = (Employee) bean ;
        employee.setAge(39);
        return employee;
    }
    
    /**
     * 在初始化方法之后执行
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 对bean对象进行初始化后置处理
        System.out.println("postProcessAfterInitialization==> "+ bean + " , " + beanName );
        Employee employee  = (Employee) bean ;
        employee.setDesc("除了嘴行啥都不行.");
        return employee ;
    }
}
