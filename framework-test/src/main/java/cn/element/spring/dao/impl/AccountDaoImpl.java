package cn.element.spring.dao.impl;

import cn.element.spring.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDaoImpl implements AccountDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int transfer(int id, int money) {
        String sql = "update `tb_account` set balance = balance + ? where id = ?";
        return jdbcTemplate.update(sql, money, id);
    }
}
