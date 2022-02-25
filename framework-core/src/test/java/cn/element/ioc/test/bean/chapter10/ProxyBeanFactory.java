package cn.element.ioc.test.bean.chapter10;

import cn.element.ioc.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * 这是一个实现接口 FactoryBean 的代理类 ProxyBeanFactory 名称，主要是模拟了
 * UserDao 的原有功能，类似于 MyBatis 框架中的代理操作。
 */
public class ProxyBeanFactory implements FactoryBean<IUserDao> {

    @Override
    public IUserDao getObject() throws Exception {
        InvocationHandler handler = (proxy, method, args) -> {
            Map<String, String> map = new HashMap<>();
            map.put("10001", "洛必达");
            map.put("10002", "牛顿");
            map.put("10003", "莱布尼茨");

            return "You've been proxied" + method.getName() + ":" + map.get(args[0].toString());
        };

        return (IUserDao) Proxy.newProxyInstance(
                                Thread.currentThread().getContextClassLoader(),
                                new Class[]{IUserDao.class},
                                handler
                          );
    }

    @Override
    public Class<?> getObjectType() {
        return IUserDao.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
