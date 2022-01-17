package cn.element.core.test.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private static final Map<String, String> map = new HashMap<>();

    static {
        map.put("10001", "洛必达");
        map.put("10002", "伯努利");
        map.put("10003", "牛顿");
    }

    public String queryUsername(String uid) {
        return map.get(uid);
    }
}
