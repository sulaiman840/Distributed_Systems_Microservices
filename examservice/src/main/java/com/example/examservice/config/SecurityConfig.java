package com.example.examservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;          // ← add this
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.POST,   "/exams/**").authenticated()
        .requestMatchers(HttpMethod.PUT,    "/exams/**").authenticated()
        .requestMatchers(HttpMethod.DELETE, "/exams/**").authenticated()
        .requestMatchers(HttpMethod.GET,    "/exams/**").authenticated()
        .requestMatchers("/exams/*/questions/**").authenticated()
        .requestMatchers("/exams/*/student-exams/**").authenticated()
        .anyRequest().denyAll()
      )
      .oauth2ResourceServer(oauth2 ->
        oauth2.jwt(Customizer.withDefaults())
      );
    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    SecretKey key = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(key).build();
  }
}
