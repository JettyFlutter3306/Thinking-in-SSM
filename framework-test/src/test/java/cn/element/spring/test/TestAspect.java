package cn.element.spring.test;

import cn.element.spring.aspect.Eatable;
import cn.element.spring.aspect.Vehicle;
import cn.element.spring.calculator.ArithmeticCalculator;
import cn.element.spring.calculator.ArithmeticCalculatorImpl;
import cn.element.spring.calculator.CalculatorLoggingHandler;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Proxy;

public class TestAspect {

    /**
     * 这两行代码可以设置环境变量,用于保存生成的代理类
     * Properties properties = System.getProperties();
     * properties.put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
     */
    @Test
    public void testCalculatorProxy() {
        //创建一个计算器实现类
        ArithmeticCalculator arithmeticCalculator = new ArithmeticCalculatorImpl();

        //创建代理对象 代理对象中对每个方法进行了增强 打印日志
        ArithmeticCalculator proxyObject = (ArithmeticCalculator) CalculatorLoggingHandler.createProxy(arithmeticCalculator);

        double add = proxyObject.add(1, 2);
        System.out.println("add = " + add);
    }
    
    @Test
    public void testCalculatorAop() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aop.xml");
        ArithmeticCalculator calculator = context.getBean("arithmeticCalculatorImpl", ArithmeticCalculator.class);
        double result = calculator.add(2, 8);
        System.out.println("result = " + result);
        
        // 判断是否是代理类
        System.out.println(Proxy.isProxyClass(calculator.getClass()));
    }
    
    @Test
    public void testTimerAspect() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aop.xml");
        Eatable person = context.getBean(Eatable.class);
        person.eat();
    }
    
    @Test
    public void testProxyTargetClass() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aop.xml");
        Vehicle car = context.getBean(Vehicle.class);
        car.move();
    }
    
    

}
