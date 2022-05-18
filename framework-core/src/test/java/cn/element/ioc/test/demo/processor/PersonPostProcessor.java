package cn.element.ioc.test.demo.processor;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.factory.config.BeanPostProcessor;
import cn.element.ioc.stereotype.Component;
import cn.element.ioc.test.demo.bean.Person;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PersonPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Person) {
            Person person = (Person) bean;
            person.setId(8080);
            log.debug("Post Process Before Initialization: {}", bean);
            return person;
        }
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Person) {
            Person person = (Person) bean;
            person.setName("伯努利");
            log.debug("Post Process After Initialization: {}", bean);
            return person;
        }
        return null;
    }
}
