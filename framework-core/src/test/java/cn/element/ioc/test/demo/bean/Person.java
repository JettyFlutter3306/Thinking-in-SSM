package cn.element.ioc.test.demo.bean;

import cn.element.ioc.beans.factory.DisposableBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Person implements Movable, DisposableBean {
    
    public static final String DEFAULT_NAME = "洛必达";
    
    private Integer id;
    
    private String name;
    
    public void init() {
        log.debug("Execute Init Method ...");
        this.id = new Random().nextInt(10000);
        this.name = DEFAULT_NAME;
    }

    @Override
    public void destroy() throws Exception {
        log.debug("Execute Destroy Method ...");
    }

    @Override
    public void move() {
        System.out.println("I can move...");
    }
}
