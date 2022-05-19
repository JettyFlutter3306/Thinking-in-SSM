package cn.element.ioc.test.demo.event;

import cn.element.ioc.context.ApplicationListener;
import cn.element.ioc.context.event.ContextRefreshedEvent;
import cn.element.ioc.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("上下文刷新事件：{}", this.getClass().getName());
    }

}
