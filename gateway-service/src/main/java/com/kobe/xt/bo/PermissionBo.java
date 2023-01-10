package com.kobe.xt.bo;

import lombok.Data;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 权限信息业务类
 * @date 2022/9/13 11:10
 */
@Data
public class PermissionBo {

    //权限id
    private String id;

    //权限名称
    private String permissionName;

    // 权限CODE
    private String permissionCode;

    //权限描述
    private String description;

    //授权链接
    private String url;

    // 系统模块
    private String systemModule;

}
