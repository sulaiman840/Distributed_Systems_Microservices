package com.example.paymentservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
  @Bean @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
