package cn.element.ioc.test.chap18.converter;

import cn.element.ioc.core.convert.converter.Converter;

public class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(String source) {
        return Integer.valueOf(source);
    }

}
