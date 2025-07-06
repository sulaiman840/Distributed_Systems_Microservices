package com.example.paymentservice.service;

import com.example.paymentservice.dto.CourseResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

@Service
public class CourseClientService {
  private final RestTemplate rt;
  private final String courseServiceName;

  public CourseClientService(RestTemplate rt,
      @Value("${course.service.name:courses-service}") String courseServiceName) {
    this.rt = rt;
    this.courseServiceName = courseServiceName;
  }

  /**
   * Fetch the CourseResponse from the Courses Service,
   * sending along the student's JWT so it passes auth.
   */
  public CourseResponse getCourse(Long courseId, String bearerToken) {
    String url = String.format("http://%s/courses/%d", courseServiceName, courseId);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(bearerToken);

    HttpEntity<Void> entity = new HttpEntity<>(headers);
    ResponseEntity<CourseResponse> resp = rt.exchange(
      url,
      HttpMethod.GET,
      entity,
      CourseResponse.class
    );
    return resp.getBody();
  }
}
