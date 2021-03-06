package cn.element.ioc.context;

import cn.element.ioc.beans.factory.HierarchicalBeanFactory;
import cn.element.ioc.beans.factory.ListableBeanFactory;
import cn.element.ioc.core.io.ResourceLoader;

/**
 * context是实现应用上下文功能的服务包
 * Application继承自ListableBeanFactory,也就继承了关于BeanFactory方法
 * 比如一些getBean()方法,另外ApplicationContext本身是Central接口
 * 目前还不需要添加一些获取ID和父类上下文
 */
public interface ApplicationContext extends ListableBeanFactory,
                                            HierarchicalBeanFactory,
                                            ResourceLoader,
                                            ApplicationEventPublisher {




}
