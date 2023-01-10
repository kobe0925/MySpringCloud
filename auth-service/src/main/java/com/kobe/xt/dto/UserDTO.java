package com.kobe.xt.dto;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2022/9/9 13:19
 */
@Data
public class UserDTO {

    /**
     * 用户id，主键
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户手机号
     */
    private String phoneNumber;

    /**
     * 用户注册时间
     */
    private LocalDateTime gmtCreate;
}
