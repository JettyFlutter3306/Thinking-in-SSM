package cn.element.ioc.context;

import java.util.EventListener;

/**
 * Interface to be implemented by application event listeners.
 * Based on the standard java.util.EventListener interface
 * for the Observer design pattern.
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * Handle an application event.
     * @param event the event to respond to
     */
    void onApplicationEvent(E event);



}
