package cn.element.orm.test.stage4;

import cn.element.orm.io.Resources;
import cn.element.orm.session.SqlSession;
import cn.element.orm.session.SqlSessionFactory;
import cn.element.orm.session.SqlSessionFactoryBuilder;
import cn.element.orm.test.stage4.dao.IUserDao;
import cn.element.orm.test.stage4.po.User;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ApiTest04 {

    private final Logger logger = LoggerFactory.getLogger(ApiTest04.class);

    @Test
    public void testSqlSessionFactory() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        User user = userDao.queryUserInfoById(1L);
        logger.info("测试结果：{}", JSON.toJSONString(user));
    }

}
