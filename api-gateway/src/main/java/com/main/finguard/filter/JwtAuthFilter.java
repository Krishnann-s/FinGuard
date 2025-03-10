package com.main.finguard.filter;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.main.finguard.util.JwtUtil;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements WebFilter{

	@Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    	System.out.println("filter");
        ServerHttpRequest request = exchange.getRequest();
        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) 
        {
            String token = authorizationHeader.substring(7);

            try {
            	System.out.println(token);
                // Validate and parse token
                Claims claims = JwtUtil.extractClaims(token);
                System.out.println("Claims : "+claims);
                System.out.println(JwtUtil.isTokenExpired(claims));
                if (!JwtUtil.isTokenExpired(claims)) {
                    String username = JwtUtil.getUsername(claims);
                    String r = JwtUtil.getRoles(claims);
                    System.out.println(r);
                    String roles[]= {r};
                    var authorities = Arrays.stream(roles)
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    SecurityContext context = new SecurityContextImpl(authenticationToken);
                    context.setAuthentication(authenticationToken);
                    request=  exchange.getRequest().mutate().
                   		 header("loggedInUser", username).build();

                    return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
                }
            } catch (Exception e) {
            	e.printStackTrace();
                return Mono.error(new RuntimeException("Invalid JWT token"));
            }
        }

        return chain.filter(exchange);
    }
}
