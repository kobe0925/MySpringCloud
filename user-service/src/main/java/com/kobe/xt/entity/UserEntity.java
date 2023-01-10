package com.kobe.xt.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id，主键
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 用户名
     */
    @Column(name = "username", nullable = false)
    private String username;

    /**
     * 用户密码
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 用户手机号
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * 用户注册时间
     */
    @Column(name = "gmt_create", nullable = false)
    private LocalDateTime gmtCreate;

}
