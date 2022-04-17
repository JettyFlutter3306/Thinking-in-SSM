package cn.element.ioc.test.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class TimerAspect {

    public static final Logger log = LoggerFactory.getLogger(TimerAspect.class);
    
    @Pointcut("execution(* cn.element.ioc.test.aspect.Person.*(..))")
    public void pointcut() {
        
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("环绕通知前置计时...");
        long start = System.currentTimeMillis();

        Object res = joinPoint.proceed();
        
        log.debug("环绕通知后置计时...");
        long end = System.currentTimeMillis();
        log.debug("程序运行用时: {}", end - start);
        return res;
    }

}
