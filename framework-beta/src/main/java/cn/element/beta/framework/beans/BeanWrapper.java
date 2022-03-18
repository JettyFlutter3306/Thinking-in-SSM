package cn.element.beta.framework.beans;

/**
 * 用于封装创建后的对象实例,代理对象(Proxy Object)或者原生对象(Original Object)
 * 都由BeanWrapper来保存
 */
public class BeanWrapper {
    
    private Object wrappedInstance;
    
    private Class<?> wrappedClass;

    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    /**
     * 返回代理以后的Class
     * 可能会是 $Proxy0
     */
    public Class<?> getWrappedClass() {
        return wrappedClass;
    }
}
