package com.cjm.shiro.boot_shiro_v1.realm;

import com.cjm.shiro.boot_shiro_v1.po.PermissionBean;
import com.cjm.shiro.boot_shiro_v1.po.RoleBean;
import com.cjm.shiro.boot_shiro_v1.po.UserBean;
import com.cjm.shiro.boot_shiro_v1.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("===执行授权===");
        // 获取对象
        Subject subject = SecurityUtils.getSubject();
        // 获取用户
        UserBean user = (UserBean) subject.getPrincipal();
        if (user != null) {
            // 授权信息
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

            // 角色
            Collection<String> rolesCollection = new HashSet<>();
            // 权限
            Collection<String> permissionCollection = new HashSet<>();

            Set<RoleBean> roles = user.getRoles();
            for (RoleBean role : roles) {
                rolesCollection.add(role.getRoleName());
                Set<PermissionBean> permissionBeans = role.getPermissions();
                for (PermissionBean permissionBean : permissionBeans) {
                    permissionCollection.add(permissionBean.getPermissionUrl());
                }
                info.addRoles(rolesCollection);
                return info;
            }

        }
        return null;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("===开始认证喽===");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        UserBean bean = userService.findByName(token.getUsername());
        if (bean == null) {
            throw new UnknownAccountException();
        }
        ByteSource credentialsSalt = ByteSource.Util.bytes(bean.getName());
        return new SimpleAuthenticationInfo(bean, bean.getPassword(), credentialsSalt, getName());
    }
}
