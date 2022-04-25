package cn.element.orm.test.dao;

import cn.element.orm.test.po.User;

public interface IUserDao {

    User queryUserInfoById(Long uId);

}
