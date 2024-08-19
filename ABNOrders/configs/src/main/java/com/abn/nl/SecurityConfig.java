package com.abn.nl;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@Configuration
public class SecurityConfig {
    private final String eurekaUri =        "/eureka/**";
    private final String configUri_prd =    "/product-client/**";
    private final String configUri_admin =  "/product-admin/**";
    private final String proxyUri=          "/api/v1/proxies/**";
    private final String searchUri =        "/api/v1/products/fetchProducts";
    private final String orderUri =         "/api/v1/products/placeOrder";
    private final String actuatorUri =         "/actuator/refresh";
    private final String searchOrderUri =        "/search/order**";
    private final String reportingUri =        "/reporting**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(autho -> autho
                        .requestMatchers(eurekaUri).hasRole("SUPERUSER")
                        .requestMatchers(configUri_prd).hasRole("SUPERUSER")
                        .requestMatchers(configUri_admin).hasRole("SUPERUSER")
                        .requestMatchers(actuatorUri).hasRole("SUPERUSER")
                        .requestMatchers(searchOrderUri).hasRole("ADMIN")
                        .requestMatchers(reportingUri).hasRole("ADMIN")
                        .requestMatchers(searchUri).authenticated()
                        .requestMatchers(orderUri).authenticated()
                        .requestMatchers(proxyUri).authenticated()
                        .anyRequest().authenticated()
                );
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
