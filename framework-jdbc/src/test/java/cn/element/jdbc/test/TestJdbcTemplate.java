package cn.element.jdbc.test;

import cn.element.jdbc.core.JdbcTemplate;
import cn.element.jdbc.core.RowMapper;
import cn.element.jdbc.pojo.User;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class TestJdbcTemplate {
    
    private JdbcTemplate jdbcTemplate;
    
    @Before
    public void before() throws SQLException {
        jdbcTemplate = new JdbcTemplate();
    }
    
    @Test
    public void testInsert() throws SQLException {
        User user = new User();
        user.setId(null)
            .setUsername("洛必达")
            .setPassword("123456")
            .setPhone("8888888")
            .setCreateTime(new Date());
        String sql = "insert into `tb_user` values(?, ?, ?, ?, ?)";
        int updated = jdbcTemplate.update(sql, user.getId(), user.getUsername(), user.getPassword(), user.getPhone(), user
                .getCreateTime());
        log.debug("updated: {}", updated);
    }
    
    @Test
    public void testSelect() throws SQLException {
        String sql = "select * from `tb_user`";
        List<User> users = jdbcTemplate.query(sql, set -> {
            Class<User> clazz = User.class;
            Field[] fields = clazz.getDeclaredFields();
            List<User> list = new ArrayList<>();

            while (set.next()) {
                User user = clazz.getConstructor().newInstance();
                for (Field field : fields) {
                    Object obj = set.getObject(StrUtil.toUnderlineCase(field.getName()));
                    field.setAccessible(true);
                    field.set(user, obj);
                }
                list.add(user);
            }

            return list;
        });
        
        users.forEach(System.out::println);
    }

}
