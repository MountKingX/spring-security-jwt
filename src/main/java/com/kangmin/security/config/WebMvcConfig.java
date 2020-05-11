package com.kangmin.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // == spring-boot make it so easy for auto-configurations ==
    // == most of things can be done in application.properties ==

    private static final long MAX_AGE_SECS = 3600;

    // == if we do development by separating frontend and backend ==
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:3001",
                        "http://localhost:4200",
                        "http://localhost:5000"
                )
                .allowedMethods(
                        "HEAD",
                        "OPTIONS",
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE"
                )
                .maxAge(MAX_AGE_SECS);
    }
}
