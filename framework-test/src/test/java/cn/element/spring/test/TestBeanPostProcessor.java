package cn.element.spring.test;

import cn.element.spring.pojo.Employee;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestBeanPostProcessor {

    @Test
    public void testBeanPostProcessor() {
        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("spring-lifecycle.xml");
        Employee employee = ctx.getBean("employee", Employee.class);
        System.out.println("生命周期 第四阶段 ====>④ 使用Bean " + employee);

        //关闭容器
        ctx.close();
    }

}
