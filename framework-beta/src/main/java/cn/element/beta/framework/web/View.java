package cn.element.beta.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * View就是前面所说的自定义模板解析引擎,核心方法就是render()
 * 在render()方法中完成对模板的渲染,最终返回浏览器能识别的字符串,通过Response输出
 */
public class View {

    /**
     * 默认响应头
     */
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";
    
    private final File viewFile;

    public View(File viewFile) {
        this.viewFile = viewFile;
    }

    public static String getDefaultContentType() {
        return DEFAULT_CONTENT_TYPE;
    }
    
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (RandomAccessFile access = new RandomAccessFile(viewFile, "r")) {
            String line;
            while ((line = access.readLine()) != null) {
                line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                Pattern pattern = Pattern.compile("$\\{[^\\}] + \\}", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    String paramName = matcher.group();
                    paramName = paramName.replaceAll("$\\{|\\}", "");
                    Object paramValue = model.get(paramName);

                    if (paramValue != null) {
                        // 把${}中间这个字符串提取出来
                        line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                        matcher = pattern.matcher(line);
                    }
                }

                sb.append(line);
            }
        }
        
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
//        response.setContentType(DEFAULT_CONTENT_TYPE);
        response.getWriter().write(sb.toString());
    }
    
    public static String makeStringForRegExp(String s) {
        return s.replace("\\", "\\\\")
                .replace("*", "\\*")
                .replace("+", "\\+")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("^", "\\^")
                .replace("$", "\\$")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("?", "\\?")
                .replace(",", "\\,")
                .replace(".", "\\.")
                .replace("&", "\\&");
    }
}
