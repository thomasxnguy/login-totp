package com.example.auth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

/**
 * Enumeration of all different authority supported by this application
 */
public enum Authority {

    ADMIN,
    USER;

    private final SimpleGrantedAuthority grantedAuthority;

    Authority() {
        this.grantedAuthority = new SimpleGrantedAuthority(name());
    }

    /**
     * Return : a collection of all authorities
     */
    public static Collection<GrantedAuthority> allAuthorities() {
        return Arrays.stream(values()).map(Authority::getAuthority).collect(toList());
    }

    /**
     * Return : a GrantedAuthority associated to Authority
     */
    public GrantedAuthority getAuthority() {
        return grantedAuthority;
    }

}