package cn.element.orm.test.stage4.dao;

import cn.element.orm.test.stage4.po.User;

public interface IUserDao {

    User queryUserInfoById(Long uId);

}
