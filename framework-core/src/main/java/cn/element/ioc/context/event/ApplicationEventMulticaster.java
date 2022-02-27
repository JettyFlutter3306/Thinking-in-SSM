package cn.element.ioc.context.event;

import cn.element.ioc.context.ApplicationEvent;
import cn.element.ioc.context.ApplicationListener;

/**
 * 在事件广播器中定义了添加监听和删除监听的方法以及一个广播事件的方法
 * multicastEvent 最终推送时间消息也会经过这个接口方法来处理谁该接收事
 * 件。
 */
public interface ApplicationEventMulticaster {

    /**
     * Add a listener to be notified of all events.
     * @param listener the listener to add
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * Remove a listener from the notification list.
     */
    void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * Multicast the given application event to appropriate listeners.
     */
    void multicastEvent(ApplicationEvent event);
}
