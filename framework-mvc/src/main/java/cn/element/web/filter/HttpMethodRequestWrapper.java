package cn.element.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HttpMethodRequestWrapper extends HttpServletRequestWrapper {

    private final String method;

    private final String trueMethod;

    public HttpMethodRequestWrapper(HttpServletRequest request, String method, String trueMethod) {
        super(request);
        this.method = method;
        this.trueMethod = trueMethod;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    public String getTrueMethod() {
        return trueMethod;
    }
}
