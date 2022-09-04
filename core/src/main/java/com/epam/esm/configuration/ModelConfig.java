package com.epam.esm.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type ModelConfig.
 * <p>
 * This class includes the configuration for prod profile that's used like main profile.
 */
@Configuration
@ComponentScan(basePackages = {"com.epam.esm"})
@EnableTransactionManagement
public class ModelConfig {
    /**
     * BCryptPasswordEncoder bCryptPasswordEncoder.
     *
     * @return the b crypt password encoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}