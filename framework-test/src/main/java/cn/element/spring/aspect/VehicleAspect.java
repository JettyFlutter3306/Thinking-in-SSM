package cn.element.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

@Slf4j
public class VehicleAspect {
    
    public void before(JoinPoint joinPoint) {
        log.debug("前置通知: 汽车开始打火发动...");
    }
    
    public void after(JoinPoint joinPoint) {
        log.debug("后置通知: 汽车开始刹车...");
    }
    
    public void afterReturning(JoinPoint joinPoint, Object result) {
        log.debug("返回通知: 汽车开始返回...");
    }
    
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {
        log.debug("异常通知: 汽车抛锚了...");
    }
    
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("环绕前置通知: 汽车加油...");
        Object result = joinPoint.proceed();
        log.debug("环绕后置通知: 汽车到站入库...");
        return result;
    }

}
