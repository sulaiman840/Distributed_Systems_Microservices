package com.example.courseservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.POST,   "/courses").authenticated()
        .requestMatchers(HttpMethod.PUT,    "/courses/**").authenticated()
        .requestMatchers(HttpMethod.DELETE, "/courses/**").authenticated()
        .requestMatchers(HttpMethod.GET,    "/courses/**").authenticated()
        .requestMatchers("/subscriptions/**").authenticated()
        .anyRequest().denyAll()
      )
      .oauth2ResourceServer(oauth2 -> oauth2
        .jwt(Customizer.withDefaults())
      );
    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    SecretKey key = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(key).build();
  }
}
