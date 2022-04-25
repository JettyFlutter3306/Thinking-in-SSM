package cn.element.orm.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 数据源工厂
 * 数据源工厂包括两部分，分别是无池化和有池化，
 * 有池化的工程继承无池化工厂，因为在 Mybatis 源码的实现类中，
 * 这样就可以减少对 Properties 统一包装的反射方式的属性处理。
 * 由于我们暂时没有对这块逻辑进行开发，只是简单的获取属性传参，
 * 所以还不能体现出这样的继承有多便捷，读者可以参考源码进行理解。
 * 源码类：UnpooledDataSourceFactory
 */
public interface DataSourceFactory {

    void setProperties(Properties props);

    DataSource getDataSource();

}
