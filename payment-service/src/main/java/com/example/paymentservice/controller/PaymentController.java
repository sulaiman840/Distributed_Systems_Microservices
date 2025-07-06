package com.example.paymentservice.controller;

import com.example.paymentservice.dto.CourseResponse;
import com.example.paymentservice.model.Payment;
import com.example.paymentservice.model.PaymentStatus;
import com.example.paymentservice.repository.PaymentRepository;
import com.example.paymentservice.service.CourseClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.http.*;








@RestController
@RequestMapping("/payments")
public class PaymentController {

  private final PaymentRepository repo;
  private final RestTemplate rt;
  private final CourseClientService courseClient;

  public PaymentController(PaymentRepository repo,
                           RestTemplate rt,
                           CourseClientService courseClient) {
    this.repo = repo;
    this.rt = rt;
    this.courseClient = courseClient;
  }

  /**
   * 1) Create a payment (PENDING)
   *    - Reject if there’s already a PENDING or PAID payment for this course+user
   *    - Must attach the student’s JWT so that the courses-service /courses/{id} call passes auth
   */
  @PostMapping
  public ResponseEntity<?> create(@RequestBody Payment dto,
                                  Authentication auth) {
    Long courseId = dto.getCourseId();
    Long userId   = dto.getUserId();

    // extract the raw JWT token string
    String token = ((Jwt) auth.getPrincipal()).getTokenValue();

    // fetch course metadata & enforce approval
    CourseResponse course;
    try {
      course = courseClient.getCourse(courseId, token);
    } catch (RestClientException e) {
      return ResponseEntity
        .badRequest()
        .body("Course not found or unavailable");
    }

    if (!course.approved()) {
      return ResponseEntity
        .badRequest()
        .body("Course not approved yet");
    }

    // enforce exact price
    if (dto.getAmount().compareTo(course.price()) != 0) {
      return ResponseEntity
        .badRequest()
        .body("Payment amount must be exactly " + course.price());
    }

    // no duplicate payments
    if (repo.findByCourseIdAndUserIdAndStatus(courseId, userId, PaymentStatus.PAID)
            .isPresent()) {
      return ResponseEntity
        .badRequest()
        .body("Already paid for this course");
    }
    if (repo.findByCourseIdAndUserIdAndStatus(courseId, userId, PaymentStatus.PENDING)
            .isPresent()) {
      return ResponseEntity
        .badRequest()
        .body("Payment already pending for this course");
    }

    // create the pending payment
    dto.setStatus(PaymentStatus.PENDING);
    Payment saved = repo.save(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  /**
   * 2) Confirm payment → PAID + notify Courses Service
   */
  @PostMapping("/{id}/confirm")
  public ResponseEntity<?> confirm(@PathVariable Long id) {
    Payment p = repo.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

    if (p.getStatus() == PaymentStatus.PAID) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Payment already confirmed");
    }

    p.setStatus(PaymentStatus.PAID);
    Payment saved = repo.save(p);

    // trigger subscription callback
    String callback = String.format(
      "http://courses-service/subscriptions/paid/%d/%d",
      saved.getCourseId(), saved.getUserId()
    );
    rt.postForEntity(callback, null, Void.class);

    return ResponseEntity.ok(saved);
  }

  /**
   * 3) Check payment status/info
   */
  @GetMapping("/{id}")
  public ResponseEntity<Payment> get(@PathVariable Long id) {
    return repo.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  /**
   * 4) Courses Service uses this to verify “hasPaid?”
   */
  @GetMapping("/by-course/{courseId}/by-user/{userId}")
  public Boolean hasPaid(@PathVariable Long courseId,
                         @PathVariable Long userId) {
    return repo.findByCourseIdAndUserIdAndStatus(
      courseId, userId, PaymentStatus.PAID
    ).isPresent();
  }
}
