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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
    
    private final Set<Object> earlyProxyReferences = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.factory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 关于 DefaultAdvisorAutoProxyCreator 类的操作主要就是把创建 AOP 代理的操作
     * 从 postProcessBeforeInstantiation 移动到 postProcessAfterInitialization 中去
     * 
     * 通过设置一些 AOP 的必备参数后，返回代理对象 new 
     * ProxyFactory(advisedSupport).getProxy() 这个代理对象中就包括间
     * 接调用了 TargetSource 中对 getTargetClass() 的获取
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!earlyProxyReferences.contains(beanName)) {
            return wrapIfNecessary(bean, beanName);
        }
        
        return bean;
    }

    protected Object wrapIfNecessary(Object bean, String beanName) {
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }

        Collection<AspectJExpressionPointcutAdvisor> advisors = factory.getBeansOfType(AspectJExpressionPointcutAdvisor.class)
                                                                       .values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            
            // 过滤匹配类
            if (!classFilter.matches(bean.getClass())) {
                continue;
            }

            AdvisedSupport advisedSupport = new AdvisedSupport();

            TargetSource targetSource = new TargetSource(bean);
            advisedSupport.setTargetSource(targetSource)
                          .setInterceptor((MethodInterceptor) advisor.getAdvice())
                          .setMatcher(advisor.getPointcut().getMethodMatcher())
                          .setProxyTargetClass(true);

            // 返回代理对象
            return new ProxyFactory(advisedSupport).getProxy();
        }

        return bean;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
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
