package cn.element.core.test.bean;

public class UserService {

    private String uid;
    private String company;
    private String location;
    private UserDao userDao;

    public UserService() {

    }

    public void queryUserInfo() {
        System.out.println("查询用户信息:  " + userDao.queryUsername(uid));
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
