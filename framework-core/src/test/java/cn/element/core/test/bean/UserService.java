package cn.element.core.test.bean;

public class UserService {

    private String uid;

    private UserDao userDao;

    public UserService() {

    }

    public void queryUserInfo() {
        System.out.println("查询用户信息:  " + userDao.queryUsername(uid));
    }
}
