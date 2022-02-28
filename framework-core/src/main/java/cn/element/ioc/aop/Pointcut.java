package cn.element.ioc.aop;

/**
 * 定义切点入口,定义用于获取ClassFilter,MethodMatcher的两个类
 * 这两个接口获取都是切点表达式提供的内容
 */
public interface Pointcut {

    /**
     * Return the ClassFilter for this pointcut.
     * @return the ClassFilter (never null)
     */
    ClassFilter getClassFilter();

    /**
     * Return the MethodMatcher for this pointcut.
     * @return the MethodMatcher (never null)
     */
    MethodMatcher getMethodMatcher();

}
