package cn.element.web.servlet;

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
    
    private ResponseEntity<?> responseEntity;

    public ModelAndView() {
    }

    public ModelAndView(String viewName) {
        this(viewName, null, null);
    }

    public ModelAndView(String viewName, Map<String, ?> model, ResponseEntity<?> responseEntity) {
        this.viewName = viewName;
        this.model = model;
        this.responseEntity = responseEntity;
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

    public ResponseEntity<?> getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity<?> responseEntity) {
        this.responseEntity = responseEntity;
    }
}
