package com.qiunan.service;

import com.qiunan.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void test() {
        System.out.println(userMapper.selectById());
    }
}
