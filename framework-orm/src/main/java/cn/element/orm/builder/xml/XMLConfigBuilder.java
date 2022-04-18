package cn.element.orm.builder.xml;

import cn.element.orm.builder.BaseBuilder;
import cn.element.orm.datasource.DataSourceFactory;
import cn.element.orm.io.Resources;
import cn.element.orm.mapping.BoundSql;
import cn.element.orm.mapping.Environment;
import cn.element.orm.mapping.MappedStatement;
import cn.element.orm.mapping.SqlCommandType;
import cn.element.orm.session.Configuration;
import cn.element.orm.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XMLConfigBuilder 核心操作在于初始化 Configuration，
 * 因为 Configuration 的使用离解析 XML 和存放是最近的操作，所以放在这里比较适合。
 * 之后就是具体的 parse() 解析操作，并把解析后的信息，
 * 通过 Configuration 配置类进行存放，包括：添加解析 SQL、注册Mapper映射器。
 * 解析配置整体包括：类型别名、插件、对象工厂、对象包装工厂、设置、环境、类型转换、映射器，
 * 但目前我们还不需要那么多，所以只做一些必要的 SQL 解析处理。
 */
public class XMLConfigBuilder extends BaseBuilder {

    private Element root;

    public XMLConfigBuilder(Reader reader) {
        // 1. 调用父类初始化Configuration
        super(new Configuration());

        // 2. dom4j 处理 xml
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析配置；类型别名、插件、对象工厂、对象包装工厂、设置、环境、类型转换、映射器
     */
    public Configuration parse() {
        try {
            // 解析环境类型
            environmentsElement(root.element("environments"));

            // 解析映射器
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return configuration;
    }

    /* 
     * 解析<environments>标签
     * 以 XMLConfigBuilder#parse 解析扩展对数据源解析操作，
     * 在 environmentsElement 方法中包括事务管理器解析和从类型注册器中读取到事务工程的实现类，
     * 同理数据源也是从类型注册器中获取。
     * 
     * 最后把事务管理器和数据源的处理，
     * 通过环境构建 Environment.Builder 存放到 Configuration 配置项中，
     * 也就可以通过 Configuration 存在的地方都可以获取到数据源了。
     * 
     * <environments default="development">
     *      <environment id="development">
     *          <transactionManager type="JDBC">
     *              <property name="..." value="..."/>
     *          </transactionManager>
     *          <dataSource type="POOLED">
     *               <property name="driver" value="${driver}"/>
     *               <property name="url" value="${url}"/>
     *               <property name="username" value="${username}"/>
     *               <property name="password" value="${password}"/>
     *          </dataSource>
     *      </environment>
     * </environments>
     */
    private void environmentsElement(Element context) throws Exception {
        String environment = context.attributeValue("default");
        List<Element> environmentList = context.elements("environment");
        
        for (Element e : environmentList) {
            String id = e.attributeValue("id");
            if (environment.equals(id)) {
                // 事务管理器
                TransactionFactory txFactory = (TransactionFactory) typeAliasRegistry.resolveAlias(e.element("transactionManager")
                                                                                                    .attributeValue("type"))
                                                                                     .newInstance();

                // 数据源
                Element dataSourceElement = e.element("dataSource");
                DataSourceFactory dataSourceFactory = (DataSourceFactory) typeAliasRegistry.resolveAlias(dataSourceElement.attributeValue("type")).newInstance();
                List<Element> propertyList = dataSourceElement.elements("property");
                Properties props = new Properties();
                
                for (Element property : propertyList) {
                    props.setProperty(property.attributeValue("name"), property.attributeValue("value"));
                }
                
                dataSourceFactory.setProperties(props);
                DataSource dataSource = dataSourceFactory.getDataSource();

                // 构建环境
                Environment.Builder environmentBuilder = new Environment.Builder(id)
                                                                        .transactionFactory(txFactory)
                                                                        .dataSource(dataSource);

                configuration.setEnvironment(environmentBuilder.build());
            }
        }
    }

    private void mapperElement(Element mappers) throws Exception {
        List<Element> mapperList = mappers.elements("mapper");
        
        for (Element e : mapperList) {
            String resource = e.attributeValue("resource");
            Reader reader = Resources.getResourceAsReader(resource);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new InputSource(reader));
            Element root = document.getRootElement();
            //命名空间
            String namespace = root.attributeValue("namespace");

            // SELECT
            List<Element> selectNodes = root.elements("select");
            for (Element node : selectNodes) {
                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();

                // ? 匹配
                Map<Integer, String> parameter = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 1; matcher.find(); i++) {
                    String g1 = matcher.group(1);
                    String g2 = matcher.group(2);
                    parameter.put(i, g2);
                    sql = sql.replace(g1, "?");
                }

                String msId = namespace + "." + id;
                String nodeName = node.getName();
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

                BoundSql boundSql = new BoundSql(sql, parameter, parameterType, resultType);
                MappedStatement mappedStatement = new MappedStatement.Builder(configuration, msId, sqlCommandType, boundSql)
                                                                     .build();
                // 添加解析 SQL
                configuration.addMappedStatement(mappedStatement);
            }

            // 注册Mapper映射器
            configuration.addMapper(Resources.classForName(namespace));
        }
    }

}
