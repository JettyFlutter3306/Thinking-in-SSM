package cn.element.orm.binding;

import cn.element.orm.session.Configuration;
import cn.element.orm.session.SqlSession;
import cn.hutool.core.lang.ClassScanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 映射器注册机
 * 
 * MapperRegistry 映射器注册类的核心主要在于提供了 ClassScanner.scanPackage 扫描包路径，
 * 调用 addMapper 方法，给接口类创建 MapperProxyFactory 映射器代理类，并写入到 knownMappers 的 HashMap 缓存中。
 * 另外就是这个类也提供了对应的 getMapper 获取映射器代理类的方法，
 * 其实这步就包装了我们上一章节手动操作实例化的过程，更加方便在 DefaultSqlSession 中获取 Mapper 时进行使用。
 */
public class MapperRegistry {
    
    private final Configuration config;

    public MapperRegistry(Configuration config) {
        this.config = config;
    }

    /**
     * 将已经添加的映射器代理加入到HashMap
     */
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();
    
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        MapperProxyFactory<T> factory = (MapperProxyFactory<T>) knownMappers.get(type);
        
        if (factory == null) {
            throw new RuntimeException("Type " + type + " is not known to the MapperRegistry");
        }

        try {
            return factory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }
    
    public <T> void addMapper(Class<?> type) {
        /* Mapper 必须是接口才会注册 */
        if (type.isInterface()) {
            if (hasMapper(type)) {
                // 如果重复添加了，报错
                throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
            }
            
            // 注册映射器代理工厂
            knownMappers.put(type, new MapperProxyFactory<>(type));
        }
    }

    /**
     * 扫描包路径,添加映射器
     */
    public void addMappers(String packageName) {
        Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }

    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }
    

}
