package com.example.auth.repository;

import com.example.auth.repository.jpa.UserTOTP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserTOTPRepository extends CrudRepository<UserTOTP, String> {

    <S extends UserTOTP> S save(S entity);

    Optional<UserTOTP> findById(String userName);

    void deleteById(String userName);
}