package cn.element.orm.test.stage5;

import cn.element.orm.io.Resources;
import cn.element.orm.session.SqlSession;
import cn.element.orm.session.SqlSessionFactory;
import cn.element.orm.session.SqlSessionFactoryBuilder;
import cn.element.orm.test.dao.IUserDao;
import cn.element.orm.test.po.User;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class ApiTest05 {

    @Test
    public void testSqlSessionFactory() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        for (int i = 0; i < 50; i++) {
            User user = userDao.queryUserInfoById(1L);
            log.info("测试结果：{}", JSON.toJSONString(user));
        }
    }

}
