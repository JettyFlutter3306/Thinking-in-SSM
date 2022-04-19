package cn.element.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Person implements Eatable {

    @Override
    public void eat() {
        try {
            log.debug("我正在吃饭...");
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
