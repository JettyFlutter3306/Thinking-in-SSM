package cn.element.web.servlet;

import cn.element.web.bind.annotation.RequestParam;
import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * HandlerAdapter主要完成请求传递到服务端的参数列表与
 * Method实参列表的对应关系,完成参数值的类型转换工作
 * 核心方法是handle()
 */
public class HandlerAdapter {
    
//    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    public boolean supports(Object handler) {
        return (handler instanceof HandlerMapping);
    }
    
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMapping handlerMapping = (HandlerMapping) handler;
        
        // 每个方法有一个参数列表,这里保存的是形参列表
        Map<String, Integer> paramMapping = new HashMap<>();
        
        // 这里给出命名参数,记住参数的位置
        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length; i++) {
            for (Annotation annotation : pa[i]) {
                if (annotation instanceof RequestParam) {
                    String paramName = ((RequestParam) annotation).value();
                    
                    if (!StrUtil.isBlank(paramName)) {
                        paramMapping.put(paramName, i);
                    }
                }
            } 
        }
        
        /*
        根据用户请求的参数信息,跟Method中的参数信息进行动态匹配
        response传进来的目的只有一个,将其赋值给方法参数,仅此而已
        只有当用户传进来的ModelAndView为空的时候,才会新建一个默认的
         */
        
        // 1.要准备好这个方法的形参列表
        Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            
            if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                paramMapping.put(type.getName(), i);
            }
        }
        
        // 2.得到自定义命名参数所在的位置,用户通过URL传过来的参数列表
        Map<String, String[]> requestMap = request.getParameterMap();
        
        // 3.构造实参列表
        Object[] paramValues = new Object[parameterTypes.length];
        for (Map.Entry<String, String[]> entry : requestMap.entrySet()) {
            String value = Arrays.toString(entry.getValue())
                                 .replaceAll("\\[|\\]", "")
                                 .replaceAll("\\s", "");
            
            if (paramMapping.containsKey(entry.getKey())) {
                int index = paramMapping.get(entry.getKey());
                
                // 因为页面传过来的值都是String类型的,而在方法中定义的类型都是不固定的
                // 所以要要针对传过来的参数进行类型转换
                paramValues[index] = caseStringValue(value, parameterTypes[index]);
            }
        }
        
        if (paramMapping.containsKey(HttpServletRequest.class.getName())) {
            int reqIndex = paramMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = request;
        }

        if (paramMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = paramMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = response;
        }
        
        // 4.从handler中取出Controller,Method,然后利用反射机制进行调用
        Method method = handlerMapping.getMethod();
        Object result;
        
        try {
            result = method.invoke(handlerMapping.getController(), paramValues);
            
            if (result == null) {
                return null;
            }
            
            boolean flag = handlerMapping.getMethod().getReturnType() == ModelAndView.class;
            if (flag) {
                return (ModelAndView) result;
            }

            ResponseEntity<Object> entity = ResponseEntity.ok(result);
            ModelAndView mv = new ModelAndView();
            mv.setResponseEntity(entity);
            return mv;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            ResponseEntity<Object> entity = ResponseEntity.serverError();
            ModelAndView mv = new ModelAndView();
            mv.setResponseEntity(entity);
            return mv;
        }
    }
    
    private Object caseStringValue(String value, Class<?> clazz) {
        if (clazz == String.class) {
            return value;
        } else if (clazz == Integer.class) {
            return Integer.valueOf(value);
        } else if (clazz == int.class) {
            return Integer.parseInt(value);
        } else {
            return null;
        }
    }
    

}
