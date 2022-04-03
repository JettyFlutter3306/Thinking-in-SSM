package cn.element.beta.example.controller;

import cn.element.beta.example.common.ResponseEntity;
import cn.element.beta.example.pojo.User;
import cn.element.beta.example.service.IUserService;
import cn.element.ioc.beans.factory.annotation.Autowired;
import cn.element.ioc.stereotype.Controller;
import cn.element.web.bind.annotation.RequestMapping;
import cn.element.web.bind.annotation.RequestParam;
import cn.element.web.bind.annotation.ResponseBody;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @ResponseBody
    @RequestMapping("/list")
    public ResponseEntity<List<User>> list() {
        List<User> list = userService.list();
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/update")
    public ResponseEntity<Void> update(User user) {
        boolean b = userService.update(user);

        if (b) {
            return ResponseEntity.ok("更新成功", null);
        }

        return ResponseEntity.badRequest();
    }

    @RequestMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam("id") Integer id) {
        boolean b = userService.delete(id);

        if (b) {
            return ResponseEntity.ok("删除成功!", null); 
        }

        return ResponseEntity.badRequest();
    }

}
