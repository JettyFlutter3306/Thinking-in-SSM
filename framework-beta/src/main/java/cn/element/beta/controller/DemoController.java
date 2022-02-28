package cn.element.beta.controller;

import cn.element.beta.service.IDemoService;
import cn.element.ioc.annotation.AutoWired;
import cn.element.mvc.annotation.Controller;
import cn.element.mvc.annotation.RequestMapping;
import cn.element.mvc.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/demo")
public class DemoController {

    @AutoWired
    private IDemoService demoService;

    @RequestMapping("/query")
    public void query(HttpServletRequest req, HttpServletResponse resp,
                      @RequestParam("name") String name) {
        String result = demoService.get(name);
        try {
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/add")
    public void add(HttpServletRequest req, HttpServletResponse resp,
                    @RequestParam("a") Integer a,
                    @RequestParam("b") Integer b) {
        try {
            resp.getWriter().write(a + "+" + b + "=" + (a + b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/remove")
    public void remove(HttpServletRequest req,
                       HttpServletResponse resp,
                       @RequestParam("id") Integer id) {
    }

}
