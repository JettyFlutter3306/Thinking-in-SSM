package cn.element.spring.test;

import cn.element.spring.config.ApplicationConfig;
import cn.element.spring.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class TestJunit5 {

    @Autowired
    private AccountService accountService;
    
    @Test
    public void testTransfer() {
        int i = accountService.transfer(1, 2, 200);
        System.out.println(i);
    }
}
