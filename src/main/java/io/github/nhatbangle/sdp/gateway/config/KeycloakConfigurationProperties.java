package io.github.nhatbangle.sdp.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties("keycloak")
public class KeycloakConfigurationProperties {

    private boolean enabled = true;
    private String realm;
    private String serverUrl;
    private String clientId;
    private String clientSecret;

}
