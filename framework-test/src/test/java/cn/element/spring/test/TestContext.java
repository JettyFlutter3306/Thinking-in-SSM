package cn.element.spring.test;

import cn.element.spring.pojo.Book;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestContext {
    
    @Test
    public void testContext() {
        // 创建一个上下文对象
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        
        // 从上下文中获取JavaBean,从容器中获取而不是使用new关键字
        Book book = context.getBean(Book.class);
        System.out.println(book);
    }

}
