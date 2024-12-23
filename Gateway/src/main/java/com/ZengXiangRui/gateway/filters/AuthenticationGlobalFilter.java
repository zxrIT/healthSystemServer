package com.ZengXiangRui.gateway.filters;

import com.ZengXiangRui.gateway.config.AuthenticationProperties;
import com.ZengXiangRui.gateway.verification.JwtVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationGlobalFilter implements GlobalFilter, Ordered {
    private final AuthenticationProperties authenticationProperties;
    private final JwtVerification jwtVerification;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    public AuthenticationGlobalFilter(AuthenticationProperties authenticationProperties, JwtVerification jwtVerification) {
        this.authenticationProperties = authenticationProperties;
        this.jwtVerification = jwtVerification;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isExclude(request.getPath().toString())) {
            return chain.filter(exchange);
        }
        String token = null;
        List<String> authorization = request.getHeaders().get("Authorization");
        if (authorization != null && !authorization.isEmpty()) {
            token = authorization.get(0);
        }
        String userId;
        try {
            userId = jwtVerification.parseJwt(token);
        } catch (Exception exception) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        ServerWebExchange serverWebExchangeUserId =
                exchange.mutate().request(builder -> builder.header("userId", userId)).build();
        return chain.filter(serverWebExchangeUserId);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private boolean isExclude(String path) {
        for (String excludePath : authenticationProperties.getExcludePaths()) {
            if (antPathMatcher.match(excludePath, path)) {
                return true;
            }
        }
        return false;
    }
}
