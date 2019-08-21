package com.cjm.shiro.boot_shiro_v1.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleBean implements Serializable {
    private String roleId;
    private String roleName;
    private Set<PermissionBean> permissions = new HashSet<>();
}
