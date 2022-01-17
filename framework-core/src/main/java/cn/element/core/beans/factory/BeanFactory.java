package cn.element.core.beans.factory;

import cn.element.core.beans.BeansException;

/**
 * 声明Bean工厂类
 * Bean工厂实现了注册Bean信息,同时在这个类中包括了获取Bean的操作
 * 首先我们需要定义 BeanFactory 这样一个 Bean 工厂，
 * 提供 Bean 的获取方法 getBean(String name)，
 * 之后这个 Bean 工厂接口由抽象类 AbstractBeanFactory 实现。
 * 这样使用模板模式的设计方式，可以统一收口通用核心方法的调用逻辑和标准定义，
 * 也就很好的控制了后续的实现者不用关心调用逻辑，按照统一方式执行。
 * 那么类的继承者只需要关心具体方法的逻辑实现即可。
 */
public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    Object getBean(String name, Object... args) throws BeansException;
}
