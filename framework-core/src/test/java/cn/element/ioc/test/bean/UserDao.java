package cn.element.ioc.test.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private static final Map<String, String> map = new HashMap<>();

//    static {
//        map.put("10001", "洛必达");
//        map.put("10002", "伯努利");
//        map.put("10003", "牛顿");
//    }

    public void initDataMethod() {
        System.out.println("执行：init-method");
        map.put("10001", "洛必达");
        map.put("10002", "伯努利");
        map.put("10003", "牛顿");
    }

    public void destroyDataMethod() {
        System.out.println("执行：destroy-method");
        map.clear();
    }

    public String queryUsername(String uid) {
        return map.get(uid);
    }
}
