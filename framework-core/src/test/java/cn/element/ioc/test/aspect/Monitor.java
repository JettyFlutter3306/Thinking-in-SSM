package cn.element.ioc.test.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 方式二: 使用继承(耦合度大,会产生代码污染),软件设计当中推荐尽量使用组合方式代替继承
public class Monitor extends Person {
    
    public static final Logger log = LoggerFactory.getLogger(Monitor.class);

    @Override
    public void eat() {
        long start = System.currentTimeMillis();
        super.eat();
        long end = System.currentTimeMillis();
        long time = end - start;
        log.debug("用时: {}", time);
    }
}
