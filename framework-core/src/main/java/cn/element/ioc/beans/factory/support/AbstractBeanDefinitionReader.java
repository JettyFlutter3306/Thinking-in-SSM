package cn.element.ioc.beans.factory.support;

import cn.element.ioc.core.io.DefaultResourceLoader;
import cn.element.ioc.core.io.ResourceLoader;

/**
 * Bean定义抽象类实现
 * 抽象类把 BeanDefinitionReader 接口的前两个方法全部实现完了，并提供了构造
 * 函数，让外部的调用使用方，把 Bean 定义注入类，传递进来。
 *
 * 这样在接口 BeanDefinitionReader 的具体实现类中，就可以把解析后的 XML 文
 * 件中的 Bean 信息，注册到 Spring 容器去了。以前我们是通过单元测试使用，调
 * 用 BeanDefinitionRegistry 完成 Bean 的注册，现在可以放到 XMl 中操作了
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    private final ResourceLoader resourceLoader;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry, new DefaultResourceLoader());
    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
