package cn.element.web.filter;

import cn.element.web.bind.annotation.RequestMethod;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class RequestMethodFilter implements Filter {
    
    public static final Set<String> ALLOWED_METHODS = new HashSet<String>() {{
        add("PUT");
        add("DELETE");
    }};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String method = request.getMethod();
        log.debug("请求方式为: {}", method);
        
        if (ALLOWED_METHODS.contains(method)) {
            HttpServletRequest requestToUse = new HttpMethodRequestWrapper(request, RequestMethod.POST.name(), method);
            filterChain.doFilter(requestToUse, servletResponse);
        } else {
            filterChain.doFilter(request, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }

}
