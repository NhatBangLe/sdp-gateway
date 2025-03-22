package io.github.nhatbangle.sdp.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app")
public class AppConfigurationProperties {

    private String corsOrigin = "*";
    private String corsMethods = "*";
    private String softwareServiceUrl;
    private String fileServiceUrl;
    private String notificationServiceUrl;

}
