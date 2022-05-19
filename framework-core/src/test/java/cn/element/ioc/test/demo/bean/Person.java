package cn.element.ioc.test.demo.bean;

import cn.element.ioc.beans.factory.DisposableBean;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class Person implements Movable, DisposableBean {
    
    public static final String DEFAULT_NAME = "洛必达";
    
    private Integer id;
    
    private String name;
    
    private String gender;
    
    public Person() {
        log.debug("生命周期 第一阶段 ====> 调用构造器 创建Bean对象");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void init() {
        log.debug("生命周期 第三阶段 Execute Init Method ...");
        this.id = new Random().nextInt(10000);
        this.name = DEFAULT_NAME;
    }

    @Override
    public void destroy() throws Exception {
        log.debug("生命周期 第五阶段 Execute Destroy Method ...");
    }

    @Override
    public void move() {
        System.out.println("I can move...");
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
