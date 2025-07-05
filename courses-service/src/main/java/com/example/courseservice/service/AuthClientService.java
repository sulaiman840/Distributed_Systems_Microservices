package com.example.courseservice.service;

import com.example.courseservice.dto.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthClientService {

  private final RestTemplate restTemplate;
  private final String authServiceName;

  public AuthClientService(
      RestTemplate restTemplate,
      @Value("${auth.service.name:auth}") String authServiceName
  ) {
    this.restTemplate   = restTemplate;
    this.authServiceName = authServiceName;
  }


  public UserResponse getUserById(Long userId, String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<UserResponse> resp = restTemplate.exchange(
      "http://" + authServiceName + "/users/" + userId,
      HttpMethod.GET,
      entity,
      UserResponse.class
    );
    return resp.getBody();
  }
}
