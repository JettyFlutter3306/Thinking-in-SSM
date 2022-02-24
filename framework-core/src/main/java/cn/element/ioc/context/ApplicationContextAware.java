package cn.element.ioc.context;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.factory.Aware;

/**
 * 实现此接口，既能感知到所属的 ApplicationContext
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext context) throws BeansException;

}
