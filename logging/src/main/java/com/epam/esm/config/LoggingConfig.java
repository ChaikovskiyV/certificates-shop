package com.epam.esm.config;

import com.epam.esm.logging.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type LoggingConfig.
 * <p>
 * This class includes configuration for logging module.
 */
@Configuration
@ComponentScan(basePackages = {"com.epam.esm"})
@EnableAspectJAutoProxy
public class LoggingConfig {
    /**
     * Logging aspect logging aspect.
     *
     * @return the logging aspect
     */
    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}