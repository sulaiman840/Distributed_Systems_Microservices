package com.example.courseservice.controller;

import com.example.courseservice.dto.*;
import com.example.courseservice.model.Course;
import com.example.courseservice.service.*;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

  private final SubscriptionService subService;
  private final CourseService courseService;
  private final AuthClientService authClient;

  public SubscriptionController(SubscriptionService subService,
                                CourseService courseService,
                                AuthClientService authClient) {
    this.subService = subService;
    this.courseService = courseService;
    this.authClient = authClient;
  }

  // 1) Student pays (handled in PaymentService), then PaymentService calls this:
  @PostMapping("/paid/{courseId}/{studentId}")
  public ResponseEntity<Void> onPayment(
      @PathVariable Long courseId,
      @PathVariable Long studentId
  ) {
    Course c = courseService.get(courseId);
    if (!c.isApproved()) {
      return ResponseEntity.badRequest().build();
    }
    subService.subscribe(courseId, studentId);
    return ResponseEntity.ok().build();
  }

  // 2) Manual subscribe attempt (will fail if not paid)
  @PostMapping("/{courseId}")
  public Mono<ResponseEntity<String>> subscribe(
      @PathVariable Long courseId,
      Authentication auth
  ) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    if (!"ROLE_STUDENT".equals(jwt.getClaimAsString("role"))) {
      return Mono.just(ResponseEntity.status(403).body("Only students can subscribe"));
    }
    Long studentId = jwt.getClaim("userId");
    return Mono.fromCallable(() -> {
        subService.subscribe(courseId, studentId);
        return ResponseEntity.ok("Subscribed successfully");
      })
      .onErrorResume(IllegalStateException.class, e ->
        Mono.just(ResponseEntity.badRequest().body(e.getMessage()))
      );
  }

  // 3) List my courses
  @GetMapping("/my")
  public Mono<ResponseEntity<List<CourseResponse>>> myCourses(Authentication auth) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    Long studentId = jwt.getClaim("userId");
    var dtos = subService.getCoursesForStudent(studentId).stream()
      .map(c -> new CourseResponse(c.getId(), c.getTeacherId(), c.getTitle(), c.getDescription(),    c.getPrice()   , c.isApproved()))
      .toList();
    return Mono.just(ResponseEntity.ok(dtos));
  }

  // 4) Teacher views subscribers
  @GetMapping("/course/{courseId}/subscribers")
  @TimeLimiter(name = "subscribersTimeLimiter", fallbackMethod = "subscribersFallback")
  public CompletableFuture<List<UserResponse>> subscribers(
      @PathVariable Long courseId,
      Authentication auth
  ) {
    return CompletableFuture.supplyAsync(() -> {
      Jwt jwt = (Jwt) auth.getPrincipal();
      if (!"ROLE_TEACHER".equals(jwt.getClaimAsString("role")))
        throw new IllegalStateException("Only teachers can view subscribers");
      var course = courseService.get(courseId);
      if (!course.getTeacherId().equals(jwt.getClaim("userId")))
        throw new IllegalStateException("Not your course");
      var token = jwt.getTokenValue();
      return subService.getStudentIdsForCourse(courseId).stream()
        .map(id -> authClient.getUserById(id, token))
        .collect(Collectors.toList());
    });
  }

  public CompletableFuture<List<UserResponse>> subscribersFallback(
      Long courseId, Authentication auth, Throwable t
  ) {
    return CompletableFuture.completedFuture(List.of());
  }
}
