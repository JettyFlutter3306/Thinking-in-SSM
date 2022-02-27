package cn.element.ioc.test.event;

import cn.element.ioc.context.ApplicationListener;

import java.util.Date;

/**
 * 这个是一个用于监听 CustomEvent 事件的监听器，这里你可以处理自己想要的操
 * 作，比如一些用户注册后发送优惠券和短信通知等。
 *
 * 另外是关于 ContextRefreshedEventListener implements
 * ApplicationListener<ContextRefreshedEvent>、
 * ContextClosedEventListener implements
 * ApplicationListener<ContextClosedEvent> 监听器
 */
public class CustomEventListener implements ApplicationListener<CustomEvent> {

    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("收到：" + event.getSource() + "消息;时间：" + new Date());
        System.out.println("消息：" + event.getId() + ":" + event.getMessage());
    }
}
