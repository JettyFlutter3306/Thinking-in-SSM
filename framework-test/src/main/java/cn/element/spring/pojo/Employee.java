package cn.element.spring.pojo;

public class Employee {

    private  String lastName ;

    private  Integer age ;

    private  String desc ;

    public Employee() {
        System.out.println("生命周期 第一阶段 ====>① 调用构造器 创建Bean对象");
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        System.out.println("生命周期 第二阶段 ====>② 调用set方法给对象的属性赋值");
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Employee [lastName=" + lastName + ", age=" + age + ", desc=" + desc + "]";
    }

    /**
     * init
     *
     * 需要在配置文件中通过 init-method 来指定.
     */
    public void  init() {
        System.out.println("生命周期 第三阶段 ====>③ 执行init方法");
    }


    /**
     * destroy
     *
     * 需要在配置文件中通过destroy-method来指定。
     */
    public void destroy() {
        System.out.println("生命周期 第五阶段 ====>⑤ 执行destroy方法");
    }

}
