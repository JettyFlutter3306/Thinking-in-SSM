package cn.element.ioc.core.convert.converter;

/**
 * A converter converts a source object of type S to a target of type T.
 * 类型转换处理接口
 * Converter、ConverterFactory、ConverterRegistry，都是用于定义类型转换操作的
 * 相关接口
 */
public interface Converter<S, T> {

    /**
     * Convert the source object of type S to target type T.
     */
    T convert(S source);
    
}
