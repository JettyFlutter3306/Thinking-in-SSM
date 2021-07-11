package cn.element.core;

public class BeansException extends Exception {

    public BeansException(String s) {

        super(s);
    }

    public BeansException(String msg, Exception e) {

        super(msg, e);
    }
}
