	package com.cg.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.cg.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .authorizeExchange(exchanges -> exchanges
//                .pathMatchers("/public/**").permitAll()   // Public routes, no auth required
                .pathMatchers("/user-service/finguard/user/login", "/user-service/finguard/user/register").permitAll() // Allow access without authentication
                .pathMatchers(HttpMethod.POST,"/user-service/finguard/user").hasRole("ROLE_user")
                .pathMatchers(HttpMethod.PUT,"/user-service/finguard/**").hasRole("ROLE_user")
//                .pathMatchers(HttpMethod.PUT,"/user-service/finguard/user/**").hasRole("ROLE_user")
//                .pathMatchers(HttpMethod.PUT,"/user-service/finguard/user/**").hasRole("ROLE_admin") 
                .pathMatchers(HttpMethod.GET,"/user-service/finguard/users").hasRole("ROLE_admin")	
                .pathMatchers(HttpMethod.GET,"/user-service/finguard/user/**").hasRole("ROLE_user")
                
                .pathMatchers(HttpMethod.PUT,"/finance-management-service/**").hasRole("ROLE_user")
                .pathMatchers(HttpMethod.POST,"/finance-management-service/**").hasRole("ROLE_user")
                .pathMatchers(HttpMethod.GET,"/finance-management-service/**").hasRole("ROLE_user")	
                .pathMatchers(HttpMethod.DELETE,"/finance-management-service/**").hasRole("ROLE_user")	
                
                .pathMatchers(HttpMethod.GET,"/investment-analytics-service/**").hasRole("ROLE_user")	
                .pathMatchers(HttpMethod.PUT,"/investment-analytics-service/**").hasRole("ROLE_user")	
                .pathMatchers(HttpMethod.DELETE,"/investment-analytics-service/**").hasRole("ROLE_user")
                .pathMatchers(HttpMethod.POST,"/investment-analytics-service/api/Portfolio").hasRole("ROLE_user")
                .pathMatchers("/order-service/loadbalance/**").hasRole("ROLE_admin") // ADMIN role only for /admin/**
                .pathMatchers("/order-service/feign/**").hasRole("ROLE_user")   // USER role only for /user/**
                .anyExchange().authenticated()              // All other routes require authentication
            )
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION) // Add JWT filter
            .csrf().disable()  // Disable CSRF for stateless APIs
            .build();
    }
}
