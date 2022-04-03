package cn.element.jdbc.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JdbcUtil {
    
    private static DataSource datasource;
    
    static {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream is = loader.getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(is);
            datasource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }
    
    public static DataSource getDatasource() {
        return datasource;
    }
    
    public static void close(ResultSet rs, Statement st, Connection con) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        
        if (st != null) {
            st.close();
        }
        
        if (con != null) {
            con.close();
        }
    }
    
    public static void close(Statement st, Connection con) throws SQLException {
        close(null, st, con);
    }
    
    public static <T> List<T> getBeans(ResultSet rs, Class<T> clazz) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            List<T> list = new ArrayList<>();

            while (rs.next()) {
                T instance = clazz.getConstructor().newInstance();
                for (Field field : fields) {
                    Object obj = rs.getObject(StrUtil.toUnderlineCase(field.getName()));
                    field.setAccessible(true);
                    field.set(instance, obj);
                }
                
                list.add(instance);
            }
            
            return list;
        } catch (SQLException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        
        return null;
    }

}
