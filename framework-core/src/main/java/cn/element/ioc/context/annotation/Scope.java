package cn.element.ioc.context.annotation;

import java.lang.annotation.*;

/**
 * 用于配置作用域的自定义注解，方便通过配置 Bean 对象注解的时候，拿到 Bean
 * 对象的作用域。不过一般都使用默认的 singleton
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    String value() default "singleton";
}
