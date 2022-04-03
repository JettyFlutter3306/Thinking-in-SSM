package cn.element.jdbc.core;

import cn.element.jdbc.util.JdbcUtil;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库访问核心类
 */
public class JdbcTemplate {
    
    private final Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public JdbcTemplate() throws SQLException {
        con = JdbcUtil.getConnection();
    }

    public <T> T query(String sql, RowMapper<T> mapper, Object... params) throws SQLException {
        try {
            ps = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            rs = ps.executeQuery();
            return mapper.mapRow(rs);
        } catch (SQLException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, ps, con);
        }
        
        return null;
    }
    
    public int update(String sql, Object... params) throws SQLException {
        try {
            ps = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(null, ps, con);
        }
        
        return 0;
    }

}
