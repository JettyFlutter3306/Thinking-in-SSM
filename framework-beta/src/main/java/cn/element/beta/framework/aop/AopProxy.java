package cn.element.beta.framework.aop;

/**
 * 代理工厂的顶层接口,提供获取代理对象的底层入口
 * 默认使用JDK动态代理
 */
public interface AopProxy {

    /**
     * 获取一个代理对象
     */
    Object getProxy();

    /**
     * 通过自定义的类加载器获得一个代理对象
     */
    Object getProxy(ClassLoader loader);

}
