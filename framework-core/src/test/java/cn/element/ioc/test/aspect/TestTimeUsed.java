package cn.element.ioc.test.aspect;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * 先举一个常见的测试方法运行时间的小例子
 */
public class TestTimeUsed {
    
    public static final Logger log = LoggerFactory.getLogger(TestTimeUsed.class);

    /**
     * 测试一个方法的运行时间
     * 方式一: 最原始的方法,直接创建对象,然后那古来直接测试时间
     */
    @Test
    public void testTimeUsed01() {
        long start = System.currentTimeMillis();
        Eatable person = new Person();
        person.eat();
        long end = System.currentTimeMillis();
        long time = end - start;
        log.debug("用时: {}", time);
    }
    
    @Test
    public void testTimeUsed02() {
        Monitor monitor = new Monitor();
        monitor.eat();
    }
    
    @Test
    public void testTimeUsed03() {
        PersonProxy proxy = new PersonProxy(new Person());
        proxy.process();
    }

    // 方式四: 动态代理
    @Test
    public void testTimeUsed04() {
        Eatable person = new Person();
        
        // lambda表达式中的proxy对象是要代理的对象
        Eatable instance = (Eatable) Proxy.newProxyInstance(
                person.getClass().getClassLoader(), 
                new Class[]{Eatable.class}, 
                (proxy, method, args) -> { 
                    long start = System.currentTimeMillis();
                    method.invoke(person, args);
                    long end = System.currentTimeMillis();
                    long time = end - start;
                    log.debug("{}方法用时: {}", method.getName(), time);
                    return proxy; 
                }
        );
        
        instance.eat();
        instance.sleep();
    }
    
    @Test
    public void testTimeUsed05() {
        Eatable person = new Person();
        person.eat();
    }
}
