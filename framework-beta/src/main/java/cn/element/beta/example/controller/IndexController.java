package cn.element.beta.example.controller;

import cn.element.ioc.stereotype.Controller;
import cn.element.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexController {
    
    @RequestMapping("/")
    public void hello(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.getWriter().write("Hello Welcome to IT world!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
