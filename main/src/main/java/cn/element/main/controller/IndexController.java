package cn.element.main.controller;

import cn.element.mvc.annotation.RequestMapping;
import cn.element.mvc.annotation.RestController;

@RestController
public class IndexController {

    @RequestMapping("/")
    public String index(){

        return "HelloWorld";
    }





}
