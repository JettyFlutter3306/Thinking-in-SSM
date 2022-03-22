package cn.element.beta.framework.aop.config;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * AOP配置封装
 * 以下配置与properties文件中的属性一一对应
 */
@Data
@Accessors(chain = true)
public class AopConfig {

    /**
     * 切面表达式
     */
    private String pointCut;

    /**
     * 前置通知方法名称
     */
    private String aspectBefore;

    /**
     * 后置通知方法名称
     */
    private String aspectAfter;

    /**
     * 要织入的切面类
     */
    private String aspectClass;

    /**
     * 异常通知方法名称
     */
    private String aspectAfterThrow;

    /**
     * 需要通知的异常类型
     */
    private String aspectAfterThrowing;
    
    
    

}
