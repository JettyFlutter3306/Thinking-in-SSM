package cn.element.ioc.beans.factory.support;

import cn.element.ioc.beans.BeansException;
import cn.element.ioc.beans.factory.DisposableBean;
import cn.element.ioc.beans.factory.ObjectFactory;
import cn.element.ioc.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在DefaultSingletonBeanRegistry中主要实现getSingleton()方法,同时实现一个受保护
 * 的addSingleton()方法,这个方法可以被继承此类的其他类调用,包括AbstractBeanFactory
 * 以及继承的DefaultListableBeanFactory调用
 * 
 * 在用于提供单例对象注册的操作的 DefaultSingletonBeanRegistry 类中，共有三个
 * 缓存对象的属性；singletonObjects、earlySingletonObjects、singletonFactories，
 * 如它们的名字一样，用于存放不同类型的对象(单例对象、早期的半成品单例对
 * 象、单例工厂对象)
 *
 * 紧接着在这三个缓存对象下提供了获取、添加和注册不同对象的方法，包括：
 * getSingleton、registerSingleton、addSingletonFactory，其实后面这两个方法都比
 * 较简单，主要是 getSingleton 的操作，它是在一层层处理不同时期的单例对象，
 * 直至拿到有效的对象。
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * Internal marker for a null singleton object:
     * used as marker value for concurrent Maps (which don't support null values).
     */
    protected static final Object NULL_OBJECT = new Object();

    /**
     * 一级缓存,普通对象
     * Cache of singleton objects: bean name --> bean instance
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();
    
    /**
     * 二级缓存，提前暴漏对象，没有完全实例化的对象
     * Cache of early singleton objects: bean name --> bean instance
     */
    protected final Map<String, Object> earlySingletonObjects = new HashMap<>();

    /**
     * 三级缓存，存放代理对象
     * Cache of singleton factories: bean name --> ObjectFactory
     */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();
    
    private final Map<String, DisposableBean> disposableBeans = new LinkedHashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        Object singletonObject = singletonObjects.get(beanName);
        
        if (null == singletonObject) {
            singletonObject = earlySingletonObjects.get(beanName);
            
            // 判断二级缓存中是否有对象，这个对象就是代理对象，因为只有代理对象才会放到三级缓存中
            if (null == singletonObject) {
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    
                    // 把三级缓存中的代理对象中的真实对象获取出来，放入二级缓存中
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }
    
    protected void addSingletonFactory(String beanName, ObjectFactory<?> factory) {
        if (!singletonObjects.containsKey(beanName)) {
            singletonFactories.put(beanName, factory);
            earlySingletonObjects.remove(beanName);
        }
    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

    public void destroySingletons() {
        Set<String> keySet = disposableBeans.keySet();
        Object[] disposableBeanNames = keySet.toArray();

        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            Object beanName = disposableBeanNames[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);

            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }
    }
}
