package cn.element.orm.test.stage2;

import cn.element.orm.binding.MapperRegistry;
import cn.element.orm.session.SqlSession;
import cn.element.orm.session.SqlSessionFactory;
import cn.element.orm.session.defaults.DefaultSqlSessionFactory;
import cn.element.orm.test.stage2.dao.IUserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ApiTest02 {

    @Test
    public void testMapperProxyFactory() {
        // 1. 注册 Mapper
        MapperRegistry registry = new MapperRegistry();
        registry.addMappers("cn.element.orm.test.stage2.dao");

        // 2. 从 SqlSession 工厂获取 Session
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(registry);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 3. 获取映射器对象,这边是个代理对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 4. 测试验证
        String res = userDao.queryUserName("10001");
        log.info("测试结果：{}", res);
    }

}
