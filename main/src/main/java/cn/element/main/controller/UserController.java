package cn.element.main.controller;

import cn.element.ioc.beans.factory.annotation.Autowired;
import cn.element.ioc.stereotype.Controller;
import cn.element.main.pojo.User;
import cn.element.main.service.UserService;
import cn.element.web.bind.annotation.RequestBody;
import cn.element.web.bind.annotation.RequestMapping;
import cn.element.web.bind.annotation.RequestMethod;
import cn.element.web.bind.annotation.RequestParam;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<User> selectAll() {
        log.debug("执行selectAll查询用户列表操作...");
        return userService.selectAll();
    }
    
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public User selectById(@RequestParam("id") Integer id) {
        log.debug("执行根据id查询用户操作...");
        return userService.selectAll().get(0);
    }
    
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(@RequestBody User user) {
        log.debug("执行插入用户操作: {}", user);
        return "插入用户操作成功!";
    }
    
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public String update(@RequestBody User user) {
        log.debug("执行更新用户操作: {}", user);
        return "更新用户操作成功!";
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String delete() {
        log.debug("执行删除用户操作...");
        return "删除用户操作成功!";
    }
    
    
}
