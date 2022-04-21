package cn.element.web.servlet;

import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.util.Locale;

/**
 * 视图解析器
 * resolveViewName()方法来获得模板所对应的View
 * 这个类主要目的是:
 *      1. 将一个静态文件变为一个动态文件
 *      2. 根据用户传送不同的参数,产生不同的结果
 * 最终输出字符串,交给Response输出
 */
public class ViewResolver {
    
    private static final String DEFAULT_TEMPLATE_SUFFIX = ".html";
    
    private final File templateRootDir;
    
    private String viewName;
    
    public ViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRootPath);
    }
    
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        this.viewName = viewName;

        if (viewName == null || StrUtil.isBlank(viewName)) {
            return null;
        }

        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);

        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));

        return new View(templateFile);
    }

    public String getViewName() {
        return viewName;
    }
}
