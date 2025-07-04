package com.example.courseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = "com.example.courseservice.model")
@EnableJpaRepositories(basePackages = "com.example.courseservice.repository")
public class CoursesServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(CoursesServiceApplication.class, args);
  }
}
