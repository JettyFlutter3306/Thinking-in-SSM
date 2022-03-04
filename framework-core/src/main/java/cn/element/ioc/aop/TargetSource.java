package cn.element.ioc.aop;

import cn.element.ioc.util.ClassUtils;

/**
 * A TargetSource is used to obtain the current "target" of
 * an AOP invocation, which will be invoked via reflection if no around
 * advice chooses to end the interceptor chain itself.
 * 被代理的目标对象
 */
public class TargetSource {

    private final Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    /**
     * Return the type of targets returned by this {@link TargetSource}.
     * Can return null, although certain usages of a
     * TargetSource might just work with a predetermined
     * target class.
     * 
     * 在 TargetSource#getTargetClass 是用于获取 target 对象的接口信息的，那么这
     * 个 target 可能是 JDK 代理 创建也可能是 CGlib 创建，为了保证都能正确的
     * 获取到结果，这里需要增加判读
     * ClassUtils.isCglibProxyClass(clazz)
     *
     * @return the type of targets returned by this {@link TargetSource}
     */
    public Class<?>[] getInterfaces() {
        Class<?> clazz = this.target.getClass();
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;

        return clazz.getInterfaces();
    }

    /**
     * Return a target instance. Invoked immediately before the
     * AOP framework calls the "target" of an AOP method invocation.
     *
     * @return the target object, which contains the join point
     */
    public Object getTarget() {
        return target;
    }

}
