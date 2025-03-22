package io.github.nhatbangle.sdp.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@EnableConfigurationProperties(AppConfigurationProperties.class)
public class AppConfig implements WebMvcConfigurer {

    @Value("${app.cors-origin}")
    private String corsOrigin;
    @Value("${app.cors-methods}")
    private String corsMethods;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(corsOrigin)
                .allowedMethods(corsMethods)
                .maxAge(3600);
    }

}
