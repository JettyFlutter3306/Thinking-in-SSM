package cn.element.beta.example.service.impl;

import cn.element.beta.example.pojo.User;
import cn.element.beta.example.service.IUserService;
import cn.element.ioc.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    public List<User> list() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "洛必达", "123456", "13770888888", new Date()));
        users.add(new User(2, "牛顿", "987654", "13950888888", new Date()));
        users.add(new User(3, "莱布尼茨", "741258", "15280888888", new Date()));
        log.info("查询用户成功: {}", users);
        return users;
    }

    public boolean update(User user) {
        log.info("修改用户: {}", user);
        return true;
    }

    public boolean delete(Integer id) {
        log.info("删除用户id: {}", id);
        return true;
    }

}
