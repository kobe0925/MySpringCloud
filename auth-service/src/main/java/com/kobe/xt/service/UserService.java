package com.kobe.xt.service;

import com.kobe.xt.dto.UserDTO;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2022/9/9 13:17
 */
public interface UserService {

    /**
     * @description: 通过手机号查询用户信息
     * @author kobe_xt
     * @date: 2022/9/21 14:28
     * @param: phoneNumber
     * @return: com.kobe.xt.dto.UserDTO
     */
    UserDTO getUserInfoByPhoneNumber(String phoneNumber);
}
