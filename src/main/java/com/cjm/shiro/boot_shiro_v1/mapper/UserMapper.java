package com.cjm.shiro.boot_shiro_v1.mapper;

import com.cjm.shiro.boot_shiro_v1.po.UserBean;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper {
    UserBean findByName(String name);

    UserBean findById(String id);
}
