package com.example.courseservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentClientService {

  private final RestTemplate rt;
  private final String paymentServiceName;

  public PaymentClientService(RestTemplate rt,
    @Value("${payment.service.name:payment-service}") String paymentServiceName) {
    this.rt = rt;
    this.paymentServiceName = paymentServiceName;
  }

  /** Returns true if PaymentService has a PAID payment for this course+user **/
  public boolean hasPaid(Long courseId, Long studentId) {
    String url = String.format(
      "http://%s/payments/by-course/%d/by-user/%d",
      paymentServiceName, courseId, studentId
    );
    try {
      return Boolean.TRUE.equals(rt.getForObject(url, Boolean.class));
    } catch (Exception e) {
      return false;
    }
  }
}
