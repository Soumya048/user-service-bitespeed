package com.bitespeed.us.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Configuration
public class AppConfig {

    // db config
    @Bean(name = "us-ds")
    @ConfigurationProperties(prefix = "spring.datasource.us-ds")
    public DataSource empPortalDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
