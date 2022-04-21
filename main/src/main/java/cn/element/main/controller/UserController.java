package cn.element.main.controller;

import cn.element.ioc.beans.factory.annotation.Autowired;
import cn.element.ioc.stereotype.Controller;
import cn.element.main.pojo.User;
import cn.element.main.service.UserService;
import cn.element.web.bind.annotation.RequestMapping;
import cn.element.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @RequestMapping("/list")
    public List<User> selectAll() {
        return userService.selectAll();
    }
    
    @RequestMapping("/select")
    public User selectById(@RequestParam("id") Integer id) {
        return userService.selectAll().get(0);
    }
    
}
