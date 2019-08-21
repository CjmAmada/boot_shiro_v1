package com.cjm.shiro.boot_shiro_v1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cjm.shiro.boot_shiro_v1.mapper")
public class BootShiroV1Application {

    public static void main(String[] args) {
        SpringApplication.run(BootShiroV1Application.class, args);
    }

}
