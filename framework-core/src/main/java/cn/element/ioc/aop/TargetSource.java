package cn.element.ioc.aop;

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
     * @return the type of targets returned by this {@link TargetSource}
     */
    public Class<?>[] getInterfaces() {
        return target.getClass().getInterfaces();
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
