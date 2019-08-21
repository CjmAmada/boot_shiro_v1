package com.cjm.shiro.boot_shiro_v1.p1;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author chenjm
 * @version V1.0
 * @Title: Demo2
 * @Package: com.cjm.shiro.boot_shiro_v1.p1
 * @Description: TOTO
 * @date 2019 2019/8/21 10:54
 **/
public class Demo2 {

    @Test
    public void test01() {
        init();
        Subject subject = login("zhang", "123");
        Assert.assertTrue(subject.hasRole("role1"));
        Assert.assertTrue(subject.hasRole("role2"));
        Assert.assertTrue(subject.hasRole("role3"));
    }

    public Subject login(String username, String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, true);
        subject.login(token);
        return subject;
    }

    public void init() {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
    }
}
