package cn.element.orm.test.stage3;

import cn.element.orm.io.Resources;
import cn.element.orm.session.SqlSession;
import cn.element.orm.session.SqlSessionFactory;
import cn.element.orm.session.SqlSessionFactoryBuilder;
import cn.element.orm.test.stage3.dao.IUserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

@Slf4j
public class ApiTest03 {

    @Test
    public void testSqlSessionFactory() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        String res = userDao.queryUserInfoById("10001");
        log.info("测试结果：{}", res);
    }

}
