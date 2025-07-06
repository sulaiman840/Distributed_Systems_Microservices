package com.example.paymentservice.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
   
        .requestMatchers(HttpMethod.GET, "/payments/by-course/**").permitAll()

        .requestMatchers(HttpMethod.POST, "/payments").hasRole("STUDENT")
 
        .requestMatchers(HttpMethod.POST, "/payments/*/confirm").permitAll()

        .requestMatchers(HttpMethod.GET, "/payments/*").hasRole("ADMIN")

        .anyRequest().authenticated()
      )
      .oauth2ResourceServer(oauth2 -> oauth2
        .jwt(jwtConfigurer -> jwtConfigurer
          .jwtAuthenticationConverter(jwtAuthenticationConverter())
        )
      );
    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    SecretKey key = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(key).build();
  }


  private JwtAuthenticationConverter jwtAuthenticationConverter() {
    var conv = new JwtAuthenticationConverter();
    conv.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
    return conv;
  }

  private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
    String role = jwt.getClaimAsString("role");
    if (role == null) {
      return List.of();
    }

    return List.of(new SimpleGrantedAuthority(role));
  }
}
