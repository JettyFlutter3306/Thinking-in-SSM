package cn.element.beta.service.impl;

import cn.element.beta.service.IDemoService;
import cn.element.mvc.annotation.Service;

@Service
public class DemoService implements IDemoService {

    @Override
    public String get(String name) {
        return "My name is " + name;
    }
}
