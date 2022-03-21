package cn.element.beta.example.service;

import cn.element.beta.example.pojo.User;

import java.util.List;

public interface IUserService {
    
    List<User> list();
    
    boolean update(User user);
    
    boolean delete(Integer id);

}
