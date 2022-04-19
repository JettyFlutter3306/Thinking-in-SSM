package cn.element.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class TimerAspect {
    
    @Pointcut("execution(* cn.element.spring.aspect.Eatable.*(..))")
    public void pointcut() {
        
    }
    
    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("环绕通知前置计时器计时...");
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        log.debug("环绕通知后置计时器计时...");
        long end = System.currentTimeMillis();
        
        log.debug("方法用时: {} ms", end - start);
        return result;
    }
    

}
