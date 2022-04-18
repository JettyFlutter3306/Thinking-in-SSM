package cn.element.spring.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class Library {

    //图书集合
    private List<Book> books;

    //图书分类列表
    private Set<String> categories;

    //图书映射
    private Map<Integer, Book> bookMap;

}
