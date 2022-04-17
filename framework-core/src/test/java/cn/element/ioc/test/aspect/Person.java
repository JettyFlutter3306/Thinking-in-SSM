package cn.element.ioc.test.aspect;

// 方式三: 使用代理(静态代理)
public class Person implements Eatable {

    @Override
    public void eat() {
        try {
            System.out.println("正在吃饭...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sleep() {
        try {
            System.out.println("正在睡觉...");
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
