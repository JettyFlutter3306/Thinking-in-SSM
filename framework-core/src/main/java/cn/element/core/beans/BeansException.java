package cn.element.core.beans;

public class BeansException extends RuntimeException {

    public BeansException(String s) {
        super(s);
    }

    public BeansException(String msg, Exception e) {
        super(msg, e);
    }
}
