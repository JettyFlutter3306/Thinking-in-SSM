package cn.element.core.beans.factory;

/**
 * 定义销毁方法的接口
 */
public interface DisposableBean {

    void destroy() throws Exception;
}
