package com.epam.esm.configuration;

import com.epam.esm.config.LoggingConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type ApplicationWebConfig.
 */
@SpringBootApplication(scanBasePackages = {"com.epam.esm"})
@EnableAuthorizationServer
@EnableResourceServer
@EntityScan(basePackages = {"com.epam.esm.entity"})
@Import({ModelConfig.class, LoggingConfig.class})
public class ApplicationWebConfig extends SpringBootServletInitializer {
    private static final String RESOURCE = "messages";
    private static final String ENCODING = "UTF-8";

    public static void main(String[] args) {
        SpringApplication.run(ApplicationWebConfig.class, args);
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(RESOURCE);
        messageSource.setDefaultEncoding(ENCODING);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}