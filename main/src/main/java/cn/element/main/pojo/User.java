package cn.element.main.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User {
    
    private Integer id;
    
    private String username;
    
    private String password;    
    
    private String phone;
    
    private Date createTime;
}
