package cn.element.ioc.test.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User implements Eatable {
    
    private Integer id;
    
    private String username;
    
    private String password;

    @Override
    public void eat() {
        System.out.println("吃饭...");
    }
}
