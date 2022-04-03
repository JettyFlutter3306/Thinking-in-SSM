package cn.element.main.controller;

import cn.element.web.bind.annotation.RequestMapping;
import cn.element.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "HelloWorld";
    }





}
