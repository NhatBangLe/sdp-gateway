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
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
@RequiredArgsConstructor
public class NotificationRoutes {

    @Value("${app.notification-service-id}")
    private String serviceId;

    @Bean
    public RouterFunction<ServerResponse> notificationServiceRouter() {
        return GatewayRouterFunctions
                .route("notification-service")
                .filter(lb(serviceId))
                .route(RequestPredicates.path("/api/v1/notification/**"), http())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationServiceSwaggerRouter() {
        return GatewayRouterFunctions
                .route("notification-service-swagger")
                .filter(lb(serviceId))
                .route(RequestPredicates.path("/aggregate/notification/v3/api-docs"), http())
                .filter(setPath("/api-docs"))
                .build();
    }

}
