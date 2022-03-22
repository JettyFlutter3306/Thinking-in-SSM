package cn.element.beta.framework.aop.support;

import cn.element.beta.framework.aop.aspect.AfterReturningAdviceInterceptor;
import cn.element.beta.framework.aop.aspect.AfterThrowingAdviceInterceptor;
import cn.element.beta.framework.aop.aspect.MethodBeforeAdviceInterceptor;
import cn.element.beta.framework.aop.config.AopConfig;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要用来解析和封装AOP配置
 */
public class AdvisedSupport {

    private Class<?> targetClass;

    private Object target;

    private Pattern pointCutClassPattern;

    private transient Map<Method, List<Object>> methodCache;

    private final AopConfig config;

    public AdvisedSupport(AopConfig config) {
        this.config = config;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public List<Object> getInterceptorsAndDynamicInterceptorAdvice(Method method, Class<?> targetClass) throws NoSuchMethodException {
        List<Object> cached = methodCache.get(method);
        
        if (cached == null) {
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());

            cached = methodCache.get(m);

            //底层逻辑，对代理方法进行一个兼容处理
            this.methodCache.put(m, cached);
        }

        return cached;
    }

    public boolean pointCutMatch() {
        return pointCutClassPattern.matcher(targetClass.toString()).matches();
    }

    private void parse() {
        String pointCut = config.getPointCut()
                                .replaceAll("\\.", "\\\\.")
                                .replaceAll("\\\\.\\*", ".*")
                                .replaceAll("\\(", "\\\\(")
                                .replaceAll("\\)", "\\\\)");

        String pointCutForClass = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClass.substring(pointCutForClass.lastIndexOf(" ") + 1));

        methodCache = new HashMap<>();
        Pattern pattern = Pattern.compile(pointCut);

        try {
            Class<?> aspectClass = Class.forName(config.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<>();

            for (Method method : aspectClass.getMethods()) {
                aspectMethods.put(method.getName(), method);
            }

            // 在这里得到的方法都是原生方法
            for (Method method : targetClass.getMethods()) {
                String methodName = method.toString();
                if (methodName.contains("throws")) {
                    methodName = methodName.substring(0, methodName.lastIndexOf("throws")).trim();
                }

                Matcher matcher = pattern.matcher(methodName);
                if (matcher.matches()) {
                    // 能满足切面规则的类,添加到AOP配置中
                    List<Object> advices = new LinkedList<>();

                    // 前置通知
                    if (config.getAspectBefore() != null && StrUtil.isNotBlank(config.getAspectBefore())) {
                        //创建一个Advice
                        advices.add(new MethodBeforeAdviceInterceptor(aspectMethods.get(config.getAspectBefore()), aspectClass
                                .newInstance()));
                    }

                    // 后置通知
                    if (config.getAspectAfter() != null && StrUtil.isNotBlank(config.getAspectAfter())) {
                        //创建一个Advice
                        advices.add(new AfterReturningAdviceInterceptor(
                                aspectMethods.get(config.getAspectAfter()),
                                aspectClass.newInstance())
                        );
                    }

                    // 抛出异常通知
                    if (config.getAspectAfterThrow() != null && StrUtil.isNotBlank(config.getAspectAfterThrow())) {
                        //创建一个Advice
                        AfterThrowingAdviceInterceptor throwingAdvice = new AfterThrowingAdviceInterceptor(
                                aspectMethods.get(config.getAspectAfterThrow()),
                                aspectClass.newInstance()
                        );
                        throwingAdvice.setThrowName(config.getAspectAfterThrowing());
                        advices.add(throwingAdvice);
                    }

                    methodCache.put(method, advices);
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

}
