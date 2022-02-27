package cn.element.ioc.beans;

public class BeansException extends RuntimeException {

    public BeansException(String s) {
        super(s);
    }

    public BeansException(String msg, Throwable e) {
        super(msg, e);
    }
}
