package cn.element.beta.example.controller;

import cn.element.beta.example.service.IModifyService;
import cn.element.beta.example.service.IQueryService;
import cn.element.beta.framework.web.ModelAndView;
import cn.element.ioc.beans.factory.annotation.Autowired;
import cn.element.ioc.stereotype.Controller;
import cn.element.web.bind.annotation.RequestMapping;
import cn.element.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    IQueryService queryService;

    @Autowired
    IModifyService modifyService;

    @RequestMapping("/query")
    public ModelAndView query(HttpServletRequest request, 
                              HttpServletResponse response,
                              @RequestParam("name") String name) {
        String result = queryService.query(name);
        
        return out(response, result);
    }

    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam("name") String name, 
                            @RequestParam("addr") String addr) {
        String result;

        try {
            result = modifyService.add(name, addr);
            return out(response, result);
        } catch (Exception e) {
//			e.printStackTrace();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("detail", e.getCause().getMessage());
//			System.out.println(Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]",""));
            model.put("stackTrace", Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", ""));
            return new ModelAndView("500", model);
        }

    }

    @RequestMapping("/remove")
    public ModelAndView remove(HttpServletRequest request, 
                               HttpServletResponse response, 
                               @RequestParam("id") Integer id) {
        String result = modifyService.remove(id);
        return out(response, result);
    }

    @RequestMapping("/edit")
    public ModelAndView edit(HttpServletRequest request, 
                             HttpServletResponse response,
                             @RequestParam("id") Integer id,
                             @RequestParam("name") String name) {
        String result = modifyService.edit(id, name);
        return out(response, result);
    }


    private ModelAndView out(HttpServletResponse resp, String str) {
        try {
            resp.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @RequestMapping("/first")
    public ModelAndView queryPage(@RequestParam("name") String name) {
        String result = queryService.query(name);
        Map<String, Object> model = new HashMap<>();
        model.put("name", name);
        model.put("data", result);
        model.put("token", "123456");
        
        return new ModelAndView("first.html", model);
    }

}
