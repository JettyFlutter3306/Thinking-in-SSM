package cn.element.ioc.test.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonProxy {

    public static final Logger log = LoggerFactory.getLogger(PersonProxy.class);
    
    public Eatable eatable;

    public PersonProxy(Eatable eatable) {
        this.eatable = eatable;
    }

    // 前置处理(前置通知)
    public long before() {
        return System.currentTimeMillis();
    }

    // 后置处理
    public long after() {
        return System.currentTimeMillis();
    }

    public void process() {
        long start = before();
        eatable.eat();
        long end = after();
        long time = end - start;
        log.debug("用时: {}", time);
    }
}
