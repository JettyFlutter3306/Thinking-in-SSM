package cn.element.web.context;

import cn.element.ioc.context.ApplicationContext;
import com.sun.istack.internal.Nullable;

import javax.servlet.ServletContext;

public interface WebApplicationContext extends ApplicationContext {

    /**
     * Context attribute to bind root WebApplicationContext to on successful startup.
     * <p>Note: If the startup of the root context fails, this attribute can contain
     * an exception or error as value. Use WebApplicationContextUtils for convenient
     * lookup of the root WebApplicationContext.
     */
    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

    /**
     * Scope identifier for request scope: "request".
     * Supported in addition to the standard scopes "singleton" and "prototype".
     */
    String SCOPE_REQUEST = "request";

    /**
     * Scope identifier for session scope: "session".
     * Supported in addition to the standard scopes "singleton" and "prototype".
     */
    String SCOPE_SESSION = "session";

    /**
     * Scope identifier for the global web application scope: "application".
     * Supported in addition to the standard scopes "singleton" and "prototype".
     */
    String SCOPE_APPLICATION = "application";

    /**
     * Name of the ServletContext environment bean in the factory.
     * @see javax.servlet.ServletContext
     */
    String SERVLET_CONTEXT_BEAN_NAME = "servletContext";

    /**
     * Name of the ServletContext init-params environment bean in the factory.
     * <p>Note: Possibly merged with ServletConfig parameters.
     * ServletConfig parameters override ServletContext parameters of the same name.
     * @see javax.servlet.ServletContext#getInitParameterNames()
     * @see javax.servlet.ServletContext#getInitParameter(String)
     * @see javax.servlet.ServletConfig#getInitParameterNames()
     * @see javax.servlet.ServletConfig#getInitParameter(String)
     */
    String CONTEXT_PARAMETERS_BEAN_NAME = "contextParameters";

    /**
     * Name of the ServletContext attributes environment bean in the factory.
     * @see javax.servlet.ServletContext#getAttributeNames()
     * @see javax.servlet.ServletContext#getAttribute(String)
     */
    String CONTEXT_ATTRIBUTES_BEAN_NAME = "contextAttributes";


    /**
     * Return the standard Servlet API ServletContext for this application.
     */
    @Nullable
    ServletContext getServletContext();

}
