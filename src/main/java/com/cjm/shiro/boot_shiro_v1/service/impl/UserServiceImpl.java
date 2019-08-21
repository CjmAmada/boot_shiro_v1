package com.cjm.shiro.boot_shiro_v1.service.impl;

import com.cjm.shiro.boot_shiro_v1.mapper.UserMapper;
import com.cjm.shiro.boot_shiro_v1.po.UserBean;
import com.cjm.shiro.boot_shiro_v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserBean findByName(String name) {
        UserBean bean = userMapper.findByName(name);
        if (bean != null) {
            bean = userMapper.findById(bean.getId());
        }
        return bean;
    }
}
