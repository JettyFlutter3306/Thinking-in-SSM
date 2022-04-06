package cn.element.orm.session;

/**
 * 这其实就是一个简单工厂的定义，在工厂中提供接口实现类的能力，
 * 也就是 SqlSessionFactory 工厂中提供的开启 SqlSession 的能力。
 */
public interface SqlSessionFactory {

    /**
     * 打开一个 session
     * @return SqlSession
     */
    SqlSession openSession();

}
