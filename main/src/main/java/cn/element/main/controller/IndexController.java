package cn.element.main.controller;

import cn.element.ioc.stereotype.Controller;
import cn.element.web.bind.annotation.RequestMapping;
import cn.element.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "欢迎来到IT世界!";
    }

}
