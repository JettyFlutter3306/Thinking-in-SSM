package cn.element.ioc.test.demo.event;

import cn.element.ioc.context.ApplicationListener;
import cn.element.ioc.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Component
public class LoginEventListener implements ApplicationListener<LoginEvent> {

    @Override
    public void onApplicationEvent(LoginEvent event) {
        log.debug("收到: {} 消息; 时间: {}", event.getSource(), LocalDateTime.now());
        log.debug("消息ID: {}; 内容: {}", event.getId(), event.getMessage());
    }
}
