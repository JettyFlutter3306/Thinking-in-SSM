package cn.element.ioc.context.support;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.factory.config.BeanPostProcessor;
import cn.element.ioc.context.ApplicationContext;
import cn.element.ioc.context.ApplicationContextAware;

public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext context;

    public ApplicationContextAwareProcessor(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(context);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
