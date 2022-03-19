package cn.element.beta.framework.web;

import java.util.Map;

public class ModelAndView {

    /**
     * 页面模板的名称
     */
    private String viewName;

    /**
     * 往页面传送的参数
     */
    private Map<String, ?> model;

    public ModelAndView(String viewName) {
        this(viewName, null);
    }

    public ModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
