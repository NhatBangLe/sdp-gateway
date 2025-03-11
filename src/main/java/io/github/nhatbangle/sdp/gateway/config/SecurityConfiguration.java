package io.github.nhatbangle.sdp.gateway.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;
import java.util.stream.Stream;

@AutoConfiguration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakConfigurationProperties.class)
public class SecurityConfiguration {

    private final KeycloakConfigurationProperties keycloakProperties;
    private final String[] policyFreeResources = {
            "/docs", "/api-docs/*", "/swagger-ui/index.html",
            "/swagger-ui/*", "/aggregate/*",
            "/v3/api-docs/*", "/swagger-resources/*",
            "/api/v1/user/auth/*"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        if (keycloakProperties.isEnabled())
            http
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                    .addFilterAfter(policyEnforcerFilter(), BearerTokenAuthenticationFilter.class);
        http
                .exceptionHandling(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    private ServletPolicyEnforcerFilter policyEnforcerFilter() {
        PolicyEnforcerConfig config = new PolicyEnforcerConfig();
        config.setRealm(keycloakProperties.getRealm());
        config.setResource(keycloakProperties.getClientId());
        config.setAuthServerUrl(keycloakProperties.getServerUrl());
        config.setCredentials(Map.of("secret", keycloakProperties.getClientSecret()));

        var pathConfigs = Stream.of(policyFreeResources)
                .map(resource -> {
                            var pathConfig = new PolicyEnforcerConfig.PathConfig();
                            pathConfig.setName("free-resource");
                            pathConfig.setType("free");
                            pathConfig.setPath(resource);
                            pathConfig.setEnforcementMode(PolicyEnforcerConfig.EnforcementMode.DISABLED);
                            return pathConfig;
                        }
                ).toList();
        config.setPaths(pathConfigs);

        return new ServletPolicyEnforcerFilter(request -> config);
    }

}
