package com.cjm.shiro.boot_shiro_v1.service;

import com.cjm.shiro.boot_shiro_v1.po.UserBean;

public interface UserService {
    UserBean findByName(String name);
}
