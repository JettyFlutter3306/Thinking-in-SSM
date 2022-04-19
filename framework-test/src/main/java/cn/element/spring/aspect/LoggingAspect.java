package cn.element.spring.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Order(1)
public class LoggingAspect {

    @Pointcut("execution(* cn.element.spring.calculator.ArithmeticCalculator.*(..))")
    public void pointcut() {

    }
    
    @Before(value = "pointcut()")
    public void beforeMethod(JoinPoint joinPoint) {
        //方法名  
        String methodName = joinPoint.getSignature().getName();
        //参数列表
        Object[] args = joinPoint.getArgs();
        System.out.println("LoggingAspect==>The method " + methodName + " begin with : " + Arrays.toString(args));
    }

    /**
     * 后置通知: 在目标方法执行之后执行. 不管目标方法有没有抛出异常都会执行. 不能获取到目标方法的返回值
     * 
     * 切入点表达式:  * cn.element.spring.calculator.ArithmeticCalculator.*(..)
     * * : 任意修饰符任意返回值
     * * : 包下的任意类
     * * : 类中的任意方法
     * ..: 方法中的任意参数列表
     */
    @After(value = "pointcut()")
    public void afterMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("LoggingAspect==>The method " + methodName + " ends .");
    }

    /**
     * 返回通知: 在目标方法正常执行结束后执行，可以获取到方法的返回值.
     * 
     * 获取目标方法的返回值: 通过returning 指定一个形参名， 
     * 在通知方法中定义对应的形参，用于接收目标方法的返回值.
     */
    @AfterReturning(value = "pointcut()", returning = "result")
    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("LoggingAspect==>The method " + methodName + " end with : " + result);

    }

    /**
     * 异常通知: 在目标方法抛出异常后执行.
     * 获取目标方法的异常信息: 通过throwing定义一个形参名，在通知方法中定义对应的形参, 用于接收目标方法抛出的异常信息.
     * 也可以灵活的通过形参的类型来设置抛出指定异常以后再执行异常通知.
     */
    @AfterThrowing(value = "pointcut()", throwing = "ex")
    public void afterThrowingMethod(JoinPoint joinPoint, ArithmeticException ex) {
        String methodName = joinPoint.getSignature().getName();

        System.out.println("LoggingAspect==>The method " + methodName + " occurs Exception : " + ex);
    }

    /**
     * 环绕通知:  环绕着整个目标方法执行，可以理解为是 前置  后置  异常 返回通知的结合.
     * 类似于动态代理InvocationHandler中的invoke方法的处理.
     */
//    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            //前置通知

            //执行目标方法
            Object result = pjp.proceed();

            //返回通知
            return result;
        } catch (Throwable e) {
            //异常通知
            e.printStackTrace();
        } finally {
            //后置通知
        }

        return -100;
    }

}
