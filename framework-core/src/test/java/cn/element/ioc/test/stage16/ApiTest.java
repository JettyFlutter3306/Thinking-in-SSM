package cn.element.ioc.test.stage16;

import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
import cn.element.ioc.test.stage16.bean.Husband;
import cn.element.ioc.test.stage16.bean.Wife;
import org.junit.jupiter.api.Test;

public class ApiTest {

    @Test
    public void testCircular() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application16.xml");

        Husband husband = applicationContext.getBean("husband", Husband.class);
        Wife wife = applicationContext.getBean("wife", Wife.class);

        System.out.println("老公的媳妇：" + husband.queryWife());
        System.out.println("媳妇的老公：" + wife.queryHusband());
    }

}
