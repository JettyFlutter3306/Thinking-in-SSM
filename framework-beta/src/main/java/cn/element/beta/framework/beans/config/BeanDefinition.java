package cn.element.beta.framework.beans.config;

/**
 * 用来存储配置文件的信息
 * 相当于保存在内存中的配置
 */
public class BeanDefinition {

    /**
     * 原生Bean的全类名
     */
    private String beanClassName;

    /**
     * 标记是否是懒加载
     */
    private boolean lazyInit = false;

    /**
     * 保存beanName,在IoC容器中存储的key
     */
    private String factoryBeanName;
    
    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
