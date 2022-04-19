package cn.element.spring.aspect;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Car implements Vehicle {
    
    @Override
    public void move() {
        log.debug("汽车正在移动...");    
    }

}
