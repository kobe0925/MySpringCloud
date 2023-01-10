package com.kobe.xt.service.impl;

import com.kobe.xt.entity.UserEntity;
import com.kobe.xt.exception.ServiceException;
import com.kobe.xt.repository.UserEntityRepository;
import com.kobe.xt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2022/12/23 11:04
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public void test() {
        try {
            userEntityRepository.findAll();
        }catch (Exception e){
            throw new ServiceException(1L,"异常",e);
        }
    }

    @Override
    @Transactional
    public void updateUserInfo() {
        Optional<UserEntity> optionalUser1 = userEntityRepository.findById("2");
        UserEntity userEntity1 = optionalUser1.get();
        userEntity1.setUsername("xiangtan003");
        userEntityRepository.save(userEntity1);

        Optional<UserEntity> optionalUser2 = userEntityRepository.findById("3");
        UserEntity userEntity2 = optionalUser2.get();
        userEntity2.setUsername("kobe003");
        userEntityRepository.save(userEntity2);
        int a = 1/0;
    }
}
