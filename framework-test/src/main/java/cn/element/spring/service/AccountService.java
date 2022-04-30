package cn.element.spring.service;

import cn.element.spring.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    
    @Autowired
    private AccountDao accountDao;

    /**
     * 转账
     * @param from  转账人
     * @param to    收款人
     * @param money 金额
     */
    @Transactional
    public int transfer(int from, int to, int money) {
        int row = accountDao.transfer(from, -money);
        row += accountDao.transfer(to, money);
        return row;    
    }
}
