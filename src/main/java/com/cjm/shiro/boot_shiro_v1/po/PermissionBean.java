package com.cjm.shiro.boot_shiro_v1.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionBean implements Serializable {
    private String permissionId;
    private String permissionName;
    private String permissionUrl;
}
