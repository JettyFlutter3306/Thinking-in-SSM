package cn.element.ioc.test.stage17;

import cn.element.ioc.context.ApplicationContext;
import cn.element.ioc.context.support.ClassPathXmlApplicationContext;
import cn.element.ioc.core.convert.converter.Converter;
import cn.element.ioc.core.convert.support.StringToNumberConverterFactory;
import cn.element.ioc.test.stage17.bean.Husband;
import cn.element.ioc.test.stage17.converter.StringToIntegerConverter;
import org.junit.Test;

public class ApiTest {

    @Test
    public void testConvert() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application17.xml");
        Husband husband = applicationContext.getBean("husband", Husband.class);
        System.out.println("测试结果：" + husband);
    }

    @Test
    public void testStringToIntegerConverter() {
        StringToIntegerConverter converter = new StringToIntegerConverter();
        Integer num = converter.convert("1234");
        System.out.println("测试结果：" + num);
    }

    @Test
    public void testStringToNumberConverterFactory() {
        StringToNumberConverterFactory converterFactory = new StringToNumberConverterFactory();
        Converter<String, Integer> stringToIntegerConverter = converterFactory.getConverter(Integer.class);
        System.out.println("测试结果：" + stringToIntegerConverter.convert("1234"));

        Converter<String, Long> stringToLongConverter = converterFactory.getConverter(Long.class);
        System.out.println("测试结果：" + stringToLongConverter.convert("1234"));
    }


}
