package cn.element.ioc.core.convert.support;

import cn.element.ioc.core.convert.converter.ConverterRegistry;

public class DefaultConversionService extends GenericConversionService {

    public DefaultConversionService() {
        addDefaultConverters(this);
    }

    public static void addDefaultConverters(ConverterRegistry registry) {
        // 添加各类型转换工厂
        registry.addConverterFactory(new StringToNumberConverterFactory());
    }

}
