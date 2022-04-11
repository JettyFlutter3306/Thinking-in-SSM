package cn.element.orm.binding;

import cn.element.orm.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代理类工厂
 * 工厂操作相当于把代理的创建给封装起来了，如果不做这层封装，
 * 那么每一个创建代理类的操作，都需要自己使用 Proxy.newProxyInstance 进行处理，那么这样的操作方式就显得比较麻烦了。
 */
public class MapperProxyFactory<T> {
    
    private final Class<T> mapperInterface;

    private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<>();

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Map<Method, MapperMethod> getMethodCache() {
        return methodCache;
    }
    
    public T newInstance(SqlSession sqlSession) {
        MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }
}
