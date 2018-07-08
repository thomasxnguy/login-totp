package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.auth.repository")
public class DataSourceConfig {

    @Bean(name = "dataSource")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName("org.h2.Driver");
        driverManagerDataSource.setUsername("DBUSER");
        driverManagerDataSource.setPassword("DBPASSWORD");
        driverManagerDataSource.setUrl("jdbc:h2:mem:demo_auth;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1");
        return driverManagerDataSource;
    }
}
