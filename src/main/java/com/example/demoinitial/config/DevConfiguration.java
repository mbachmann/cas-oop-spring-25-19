package com.example.demoinitial.config;

import com.example.demoinitial.utils.HasLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Defines a Bean for the dev-Profile
 */
@Configuration
@Profile("dev")
// @ConditionalOnClass(name = {"org.h2.Driver"})
public class DevConfiguration implements HasLogger {

    public DevConfiguration() {
        getLogger().info("Dev Configuration Class");
    }

}
