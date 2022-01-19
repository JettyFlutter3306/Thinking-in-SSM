package cn.element.core.context;

import cn.element.core.beans.factory.ListableBeanFactory;

/**
 * context是实现应用上下文功能的服务包
 * Application继承自ListableBeanFactory,也就继承了关于BeanFactory方法
 * 比如一些getBean()方法,另外ApplicationContext本身是Central接口
 * 目前还不需要添加一些获取ID和父类上下文
 */
public interface ApplicationContext extends ListableBeanFactory {




}
