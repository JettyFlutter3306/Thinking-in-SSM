package cn.element.beta.example.controller;

import cn.element.beta.framework.web.ModelAndView;
import cn.element.ioc.stereotype.Controller;
import cn.element.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    
    @RequestMapping("/")
    public ModelAndView toIndex() {
        return new ModelAndView("index.html");
    }

}
