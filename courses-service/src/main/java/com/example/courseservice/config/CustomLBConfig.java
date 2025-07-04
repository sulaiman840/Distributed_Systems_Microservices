package com.example.courseservice.config;

import com.example.courseservice.config.CustomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

@Configuration
@ConditionalOnBean(ReactiveDiscoveryClient.class)
public class CustomLBConfig {

  /**
   * ننشئ ServiceInstanceListSupplier بواسطة الـ builder
   * مع DiscoveryClient و health-checks، ونجتزئ الـ ApplicationContext
   */
  @Bean
  public ServiceInstanceListSupplier serviceInstanceListSupplier(
      ConfigurableApplicationContext context
  ) {
    return ServiceInstanceListSupplier.builder()
      .withDiscoveryClient()    // يدمج ReactiveDiscoveryClient داخلياً
      .withHealthChecks()       // يدعم health checks إن فعلتها
      .build(context);          // لا تنسّ تمرير الـ context هنا!
  }

  /**
   * ثم نربط الـ supplier بـ CustomLoadBalancer
   */
  @Bean
  public ReactorServiceInstanceLoadBalancer customAuthServiceLoadBalancer(
      ServiceInstanceListSupplier supplier
  ) {
    return new CustomLoadBalancer(supplier);
  }

}
