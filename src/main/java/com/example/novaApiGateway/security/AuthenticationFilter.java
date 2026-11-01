package com.example.novaApiGateway;


import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter {

    private final JWTValidationUtil jwtValidationUtil;

    public AuthenticationFilter(JWTValidationUtil jwtValidationUtil) {
        this.jwtValidationUtil = jwtValidationUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path =exchange.getRequest().getURI().getPath();

        if (path.contains("/api/auth"))
            return chain.filter(exchange);
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return onError(exchange,"not authorized", HttpStatus.UNAUTHORIZED);
        String token = authHeader.substring(7);
        try {
            jwtValidationUtil.validateToken(token);
        }
        catch (ExpiredJwtException e) {
            System.out.println("token expired :" + e.getMessage());
            return onError(exchange,"token expired",HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            System.out.println("token validation error :" + e.getMessage());
            return onError(exchange,"token validation error",HttpStatus.FORBIDDEN);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus httpStatus) {
        System.out.println("UNAUTHENTICATED REQ: " + message);
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }
}
