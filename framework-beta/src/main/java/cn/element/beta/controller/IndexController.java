package cn.element.beta.controller;

import cn.element.mvc.annotation.Controller;
import cn.element.mvc.annotation.RequestMapping;
import cn.element.mvc.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexController {

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response, @RequestParam("name") String name) {
        String result = "hello welcome " + name + " to IT world";

        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
