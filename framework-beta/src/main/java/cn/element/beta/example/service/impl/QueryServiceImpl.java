package cn.element.beta.example.service.impl;

import cn.element.beta.example.service.IQueryService;
import cn.element.ioc.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class QueryServiceImpl implements IQueryService {
    
    @Override
    public String query(String name) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(new Date());

        return "{name: \"" + name + "\", time: \"" + time + "\"}";
    }
}
