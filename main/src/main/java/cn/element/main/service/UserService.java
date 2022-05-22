package cn.element.main.service;

import cn.element.ioc.stereotype.Service;
import cn.element.main.pojo.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {
    
    public List<User> selectAll() {
        User[] users = new User[]{
            new User(1, "洛必达", "123456", "13770365626", LocalDate.now()),        
            new User(2, "牛顿", "123456", "13770365626", LocalDate.now()),        
            new User(3, "莱布尼茨", "123456", "13770365626", LocalDate.now()),        
            new User(4, "伯努利", "123456", "13770365626", LocalDate.now()),        
        };
        
        return Stream.of(users).collect(toList());
    }
}
