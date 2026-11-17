package soa.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import io.jsonwebtoken.Claims;
import soa.service.JwtService;

@Configuration
public class SecurityConfig {

    JwtService JWT;

    public SecurityConfig(JwtService JWT) {
        this.JWT = JWT;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // User APIs are accessible without authentication
                .requestMatchers("/user/**")
                .permitAll()

                // ADMIN can create books
                .requestMatchers(HttpMethod.POST, "/books/**")
                .hasRole("ADMIN")

                // ADMIN and USER can view books
                .requestMatchers(HttpMethod.GET, "/books/**")
                .hasAnyRole("ADMIN", "USER")

                // ADMIN can update books
                .requestMatchers(HttpMethod.PUT, "/books/**")
                .hasRole("ADMIN")

                // ADMIN can delete books
                .requestMatchers(HttpMethod.DELETE, "/books/**")
                .hasRole("ADMIN")

                // All other URLs require authentication
                .anyRequest()
                .authenticated()
            )

            // Configure JWT authentication
            .oauth2ResourceServer(oauth -> oauth
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder())
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {

        return token -> {

            // Validate the JWT token using JwtService
            Claims claims = JWT.validateToken(token);

            // Convert the validated claims into Spring Security Jwt
            return new Jwt(
                token,
                claims.getIssuedAt().toInstant(),
                claims.getExpiration().toInstant(),
                Map.of("alg", "HS256"),
                claims
            );
        };
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {

        // Read the role from the "role" claim in the JWT
        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("role");

        // Convert USER/ADMIN into ROLE_USER/ROLE_ADMIN
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        // Use username as the authenticated user's principal
        converter.setPrincipalClaimName("username");

        // Set the role converter
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }
}