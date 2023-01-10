package com.kobe.xt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kobe.xt.dto.UserDTO;
import com.kobe.xt.entity.UserEntity;
import com.kobe.xt.repository.UserEntityRepository;
import com.kobe.xt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2022/9/9 13:17
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public UserDTO getUserInfoByPhoneNumber(String phoneNumber) {
        List<UserEntity> userEntityList = userEntityRepository.findByPhoneNumber(phoneNumber);
        return BeanUtil.copyProperties(userEntityList.get(0),UserDTO.class);
    }
}
