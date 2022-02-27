package cn.element.ioc.context.event;

import cn.element.ioc.context.ApplicationContext;
import cn.element.ioc.context.ApplicationEvent;

/**
 * ApplicationContextEvent 是定义事件的抽象类，所有的事件包括关闭、刷新，以及
 * 用户自己实现的事件，都需要继承这个类。
 *
 * ContextClosedEvent、ContextRefreshedEvent，分别是 Spring 框架自己实现的两
 * 个事件类，可以用于监听刷新和关闭动作。
 */
public class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(Object source) {
        super(source);
    }

    /**
     * Get the ApplicationContext that the event was raised for.
     */
    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}
