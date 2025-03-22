package io.github.nhatbangle.sdp.gateway.route;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
@RequiredArgsConstructor
public class SoftwareRoutes {

    @Value("${app.software-service-url}")
    private String serviceUrl;

    @Bean
    public RouterFunction<ServerResponse> softwareServiceRouter() {
        return GatewayRouterFunctions
                .route("software-service")
                .route(RequestPredicates.path("/api/v1/software/**"), http(serviceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> softwareServiceSwaggerRouter() {
        return GatewayRouterFunctions
                .route("software-service-swagger")
                .route(RequestPredicates.path("/aggregate/software/v3/api-docs"), http(serviceUrl))
                .filter(setPath("/api-docs"))
                .build();
    }

}
