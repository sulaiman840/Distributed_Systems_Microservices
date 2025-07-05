package com.example.authservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;                  
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.authservice.service.UserDetailsServiceImpl;

@Configuration
public class SecurityConfig {
  private final JwtUtil jwtUtil;
  private final UserDetailsServiceImpl uds;

  public SecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl uds) {
    this.jwtUtil = jwtUtil;
    this.uds     = uds;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()  
        .requestMatchers(HttpMethod.POST, "/users/by-role").hasRole("ADMIN")
        .anyRequest().authenticated()
      )
      .addFilterBefore(
         new JwtAuthenticationFilter(jwtUtil, uds),
         org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
      );

    return http.build();
  }

  @Bean public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
    return cfg.getAuthenticationManager();
  }
}
