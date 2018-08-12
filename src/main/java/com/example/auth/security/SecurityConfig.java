package com.example.auth.security;

import com.example.auth.security.otp.CustomWebAuthenticationDetailsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

/**
 * Configuration class for Spring Security.
 * For simplification we use an in-memory DB for storing credentials.
 * H2 console is accessible with "ADMIN" authority.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin().loginPage("/login").permitAll()
                .authenticationDetailsSource(authenticationDetailsSource)
                .and().exceptionHandling().accessDeniedPage("/denied").and()
                .authorizeRequests()
                .mvcMatchers("/profile*").hasAnyRole("USER", "ADMIN")
                .mvcMatchers("/h2/**").hasAnyRole("ADMIN")
                .antMatchers("/**").permitAll()
                .and()
                .logout().permitAll().logoutSuccessUrl("/")
                .and()
                .csrf().disable()
                .headers().frameOptions().disable();

    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username,password,enabled from users where username=?")
                .authoritiesByUsernameQuery(
                        "select username,role from user_roles where username=?")
                .passwordEncoder(passwordEncoder);;

    }

}

