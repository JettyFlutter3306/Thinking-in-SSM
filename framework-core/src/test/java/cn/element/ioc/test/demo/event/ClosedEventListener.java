package cn.element.ioc.test.demo.event;

import cn.element.ioc.context.ApplicationListener;
import cn.element.ioc.context.event.ContextClosedEvent;
import cn.element.ioc.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.debug("上下文关闭事件：{}", this.getClass().getName());
    }

}
