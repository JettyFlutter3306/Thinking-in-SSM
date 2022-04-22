package cn.element.web.test;

import cn.element.web.bind.annotation.RequestMethod;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TestRequestMethod {
    
    @Test
    public void testRequestMethod() {
        System.out.println(RequestMethod.GET.name());    
    }
    
    @Test
    public void testCharacterEncoding() {
        System.out.println(StandardCharsets.UTF_8.name());
    }

}
