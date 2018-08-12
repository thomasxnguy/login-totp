package com.example.auth.repository.jpa;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users_totp")
public class UserTOTP {

    @Id
    @Column(name = "username", length = 45)
    String userName;

    @Column(name = "secret", length = 200)
    String secret;

    @Column(name = "enabled")
    boolean enabled;
}
