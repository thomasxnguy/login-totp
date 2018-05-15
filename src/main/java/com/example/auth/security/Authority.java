package com.example.auth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

public enum Authority {

    ADMIN,
    USER;

    private final SimpleGrantedAuthority grantedAuthority;

    Authority() {
        this.grantedAuthority = new SimpleGrantedAuthority(name());
    }

    public static Collection<GrantedAuthority> allAuthorities() {
        return Arrays.stream(values()).map(Authority::getAuthority).collect(toList());
    }

    public GrantedAuthority getAuthority() {
        return grantedAuthority;
    }

}