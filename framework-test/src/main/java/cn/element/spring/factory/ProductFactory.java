package cn.element.spring.factory;

import cn.element.spring.pojo.Book;
import org.springframework.beans.factory.FactoryBean;

/**
 * Spring中有两种类型的bean，一种是普通bean，另一种是工厂bean，即FactoryBean
 * 工厂bean跟普通bean不同，其返回的对象不是指定类的一个实例，
 * 其返回的是该工厂bean的getObject方法所返回的对象
 * 工厂bean必须实现org.springframework.beans.factory.FactoryBean接口
 */
public class ProductFactory implements FactoryBean<Book> {

    @Override
    public Book getObject() throws Exception {
        Book book = new Book();
        book.setIsbn("1");
        book.setBookName("aaa");
        return book;
    }

    @Override
    public Class<?> getObjectType() {
        return Book.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
