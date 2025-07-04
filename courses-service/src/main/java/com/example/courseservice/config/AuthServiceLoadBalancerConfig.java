package com.example.courseservice.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@LoadBalancerClient(
  name = "auth",            // <— the Eureka service ID
  configuration = CustomLBConfig.class
)
public class AuthServiceLoadBalancerConfig {
  // empty: marker annotation only
}
