package cn.element.ioc.core.convert.support;

import cn.element.ioc.core.convert.converter.Converter;
import cn.element.ioc.core.convert.converter.ConverterFactory;
import cn.element.ioc.util.NumberUtils;
import com.sun.istack.internal.Nullable;

public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {

    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToNumber<>(targetType);
    }
    
    private static final class StringToNumber<T extends Number> implements Converter<String, T> {
        
        private final Class<T> targetType;

        public StringToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        @Nullable
        public T convert(String source) {
            if (source.isEmpty()) {
                return null;
            }
            
            return NumberUtils.parseNumber(source, targetType);
        }
    }
}
