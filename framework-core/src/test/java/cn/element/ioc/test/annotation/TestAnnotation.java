package cn.element.ioc.test.annotation;

import cn.element.ioc.stereotype.Component;
import cn.element.ioc.stereotype.Controller;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class TestAnnotation {
    
    @Controller
    public static class StudentController {
        
    }
    
    @Test
    public void testAnnotationPresent() {
        Class<StudentController> clazz = StudentController.class;
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> aClass = annotation.annotationType();
            if (aClass.isAnnotationPresent(Component.class)) {
                System.out.println("存在Component注解!");
            }
        }
    }

}
