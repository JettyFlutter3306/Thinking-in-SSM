package cn.element.ioc.beans.factory;

/**
 * 定义接口 Aware，在 Spring 框架中它是一种感知标记性接口，具体的子类定义和
 * 实现能感知容器中的相关对象。也就是通过这个桥梁，向具体的实现类中提供容器
 * 服务
 * Marker superinterface indicating that a bean is eligible to be
 * notified by the Spring container of a particular framework object
 * through a callback-style method. Actual method signature is
 * determined by individual subinterfaces, but should typically
 * consist of just one void-returning method that accepts a single
 * argument.
 *
 * 在 Spring 中有特别多类似这样的标记接口的设计方式，它们的存在就像是一种标
 * 签一样，可以方便统一摘取出属于此类接口的实现类，通常会有 instanceof 一起
 * 判断使用。
 *
 * 标记性接口,实现该接口可以被Spring容器感知
 */
public interface Aware {

}
