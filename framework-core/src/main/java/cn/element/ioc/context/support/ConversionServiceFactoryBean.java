package cn.element.ioc.context.support;

import cn.element.ioc.beans.factory.FactoryBean;
import cn.element.ioc.beans.factory.InitializingBean;
import cn.element.ioc.core.convert.ConversionService;
import cn.element.ioc.core.convert.converter.Converter;
import cn.element.ioc.core.convert.converter.ConverterFactory;
import cn.element.ioc.core.convert.converter.ConverterRegistry;
import cn.element.ioc.core.convert.converter.GenericConverter;
import cn.element.ioc.core.convert.support.DefaultConversionService;
import cn.element.ioc.core.convert.support.GenericConversionService;
import com.sun.istack.internal.Nullable;

import java.util.Set;

public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {
    
    @Nullable
    private Set<?> converters;
    
    @Nullable
    private GenericConversionService conversionService;

    @Override
    public ConversionService getObject() throws Exception {
        return conversionService;
    }

    @Override
    public Class<?> getObjectType() {
        return conversionService.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        conversionService = new DefaultConversionService();
        registerConverters(converters, conversionService);
    }

    private void registerConverters(Set<?> converters, ConverterRegistry registry) {
        if (converters != null) {
            for (Object converter : converters) {
                if (converter instanceof GenericConverter) {
                    registry.addConverter((GenericConverter) converter);
                } else if (converter instanceof Converter<?, ?>) {
                    registry.addConverter((Converter<?, ?>) converter);
                } else if (converter instanceof ConverterFactory<?, ?>) {
                    registry.addConverterFactory((ConverterFactory<?, ?>) converter);
                } else {
                    throw new IllegalArgumentException("Each converter object must implement one of the " +
                            "Converter, ConverterFactory, or GenericConverter interfaces");
                }
            }
        }
    }

    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }
}
