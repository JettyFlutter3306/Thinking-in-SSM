package cn.element.orm.session.defaults;

import cn.element.orm.binding.MapperRegistry;
import cn.element.orm.mapping.MappedStatement;
import cn.element.orm.session.Configuration;
import cn.element.orm.session.SqlSession;

/**
 * 通过 DefaultSqlSession 实现类对 SqlSession 接口进行实现。
 * getMapper 方法中获取映射器对象是通过 MapperRegistry 类进行获取的，后续这部分会被配置类进行替换。
 * 在 selectOne 中是一段简单的内容返回，目前还没有与数据库进行关联，这部分在我们渐进式的开发过程中逐步实现。
 */
public class DefaultSqlSession implements SqlSession {
    
    private final Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        return (T) ("你被代理了！" + "\n方法：" + statement + "\n入参：" + parameter + "\n待执行SQL：" + mappedStatement.getSql());
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
