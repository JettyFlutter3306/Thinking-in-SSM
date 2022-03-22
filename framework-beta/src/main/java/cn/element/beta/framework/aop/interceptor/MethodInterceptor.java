package cn.element.beta.framework.aop.interceptor;


/**
 * 方法拦截器顶层接口
 */
public interface MethodInterceptor {
    
    Object invoke(MethodInvocation invocation) throws Throwable;

}
