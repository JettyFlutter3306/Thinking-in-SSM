package cn.element.ioc.test.common;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.factory.config.BeanPostProcessor;
import cn.element.ioc.test.bean.UserService;

public class IBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("userService".equals(beanName)) {
            UserService userService = (UserService) bean;
            userService.setLocation("改为：北京");
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
