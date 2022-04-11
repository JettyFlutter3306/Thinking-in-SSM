package cn.element.orm.session;

import cn.element.orm.builder.xml.XMLConfigBuilder;
import cn.element.orm.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

/**
 * SqlSessionFactoryBuilder 是作为整个 Mybatis 的入口类，通过指定解析XML的IO，引导整个流程的启动。
 * 从这个类开始新增加了 XMLConfigBuilder、Configuration 两个处理类，
 * 分别用于解析 XML 和串联整个流程的对象保存操作。
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }

}
