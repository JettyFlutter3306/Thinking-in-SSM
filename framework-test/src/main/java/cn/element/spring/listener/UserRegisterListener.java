package cn.element.spring.listener;

import cn.element.spring.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRegisterListener {
    
    @EventListener
    public void listen(UserRegisterEvent event) {
        log.debug("{}", event);
    }
}
