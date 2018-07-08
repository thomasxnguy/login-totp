package com.example.auth.repository;

import com.example.auth.repository.jpa.UserTOTP;
import org.hibernate.mapping.PrimaryKey;
import org.springframework.data.repository.CrudRepository;


public interface UserTOTPRepository extends CrudRepository<UserTOTP, PrimaryKey> {

    <S extends UserTOTP> S save(S entity);

    UserTOTP findOne(String userName);

    void delete(String userName);
}
