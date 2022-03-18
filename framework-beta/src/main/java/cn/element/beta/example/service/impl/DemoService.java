package cn.element.beta.example.service.impl;

import cn.element.beta.example.service.IDemoService;
import cn.element.ioc.stereotype.Service;

@Service
public class DemoService implements IDemoService {

    @Override
    public String get(String name) {
        return "My name is " + name;
    }
}
