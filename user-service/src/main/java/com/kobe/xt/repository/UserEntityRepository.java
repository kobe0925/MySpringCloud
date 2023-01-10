package com.kobe.xt.repository;

import com.kobe.xt.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserEntityRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

}