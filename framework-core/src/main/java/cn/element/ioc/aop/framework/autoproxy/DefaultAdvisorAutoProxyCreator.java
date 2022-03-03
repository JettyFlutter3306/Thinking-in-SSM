package cn.element.ioc.aop.framework.autoproxy;

import cn.element.ioc.aop.*;
import cn.element.ioc.aop.aspectj.AspectJExpressionPointcutAdvisor;
import cn.element.ioc.aop.framework.ProxyFactory;
import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.PropertyValues;
import cn.element.ioc.beans.factory.BeanFactory;
import cn.element.ioc.beans.factory.BeanFactoryAware;
import cn.element.ioc.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.element.ioc.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * 这个 DefaultAdvisorAutoProxyCreator 类的主要核心实现在于
 * postProcessBeforeInstantiation 方法中，从通过 beanFactory.getBeansOfType 获
 * 取 AspectJExpressionPointcutAdvisor 开始
 * 
 * 获取了 advisors 以后就可以遍历相应的 AspectJExpressionPointcutAdvisor 填充对
 * 应的属性信息，包括：目标对象、拦截方法、匹配器，之后返回代理对象即可
 * 
 * 那么现在调用方获取到的这个 Bean 对象就是一个已经被切面注入的对象了，当调
 * 用方法的时候，则会被按需拦截，处理用户需要的信息
 */
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor,
                                                       BeanFactoryAware {
    
    private DefaultListableBeanFactory factory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.factory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (isInfrastructureClass(beanClass)) {
            return null;
        }

        Collection<AspectJExpressionPointcutAdvisor> advisors = factory.getBeansOfType(AspectJExpressionPointcutAdvisor.class)
                                                                       .values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter filter = advisor.getPointcut().getClassFilter();
            
            if (!filter.matches(beanClass)) {
                continue;
            }

            AdvisedSupport support = new AdvisedSupport();
            TargetSource source = null;

            try {
                source = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            
            support.setTargetSource(source)
                   .setInterceptor((MethodInterceptor) advisor.getAdvice())
                   .setMatcher(advisor.getPointcut().getMethodMatcher())
                   .setProxyTargetClass(false);
            
            return new ProxyFactory(support).getProxy();
        }
        
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) 
                || Pointcut.class.isAssignableFrom(beanClass) 
                || Advisor.class.isAssignableFrom(beanClass);
    }
}
