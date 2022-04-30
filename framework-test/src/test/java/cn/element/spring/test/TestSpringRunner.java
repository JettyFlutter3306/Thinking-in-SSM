package cn.element.spring.test;

import cn.element.spring.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:application.xml")
public class TestSpringRunner {

    @Autowired
    private AccountService accountService;

    @Test
    public void testTransfer() {
        int i = accountService.transfer(1, 2, 200);
        System.out.println(i);
    }
}
