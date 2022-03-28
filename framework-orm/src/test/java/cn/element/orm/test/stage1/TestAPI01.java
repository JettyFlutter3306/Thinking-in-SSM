package cn.element.orm.test.stage1;

import cn.element.orm.binding.MapperProxyFactory;
import cn.element.orm.test.stage1.dao.IUserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestAPI01 {
    
    @Test
    public void testMapperFactory() {
        MapperProxyFactory<IUserDao> factory = new MapperProxyFactory<>(IUserDao.class);
        Map<String, String> sqlSession = new HashMap<>();
        
        sqlSession.put("cn.element.orm.test.stage1.dao.IUserDao.queryUsername", "模拟执行Mapper.xml中SQL语句操作: 查询用户姓名");
        sqlSession.put("cn.element.orm.test.stage1.dao.IUserDao.queryUserAge", "模拟执行Mapper.xml中SQL语句操作: 查询用户年龄");

        IUserDao userDao = factory.newInstance(sqlSession);
        
        String result = userDao.queryUsername("10001");
        log.info("测试结果: name: {}", result);  
    }

}
