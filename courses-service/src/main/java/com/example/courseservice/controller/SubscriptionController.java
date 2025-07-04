package com.example.courseservice.controller;

import com.example.courseservice.dto.CourseResponse;
import com.example.courseservice.dto.UserResponse;
import com.example.courseservice.service.AuthClientService;
import com.example.courseservice.service.CourseService;
import com.example.courseservice.service.SubscriptionService;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
    this.subService    = subService;
    this.courseService = courseService;
    this.authClient    = authClient;
  }
  // 1) الطالب يشترك في دورة
  @PostMapping("/{courseId}")
  public Mono<ResponseEntity<String>> subscribe(
      @PathVariable Long courseId,
      Authentication auth
  ) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    if (!"ROLE_STUDENT".equals(jwt.getClaimAsString("role"))) {
      return Mono.just(ResponseEntity
        .status(403)
        .body("Only students can subscribe"));
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

  // 2) الطالب يسترجع دوراته
  @GetMapping("/my")
  public Mono<ResponseEntity<List<CourseResponse>>> myCourses(Authentication auth) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    if (!"ROLE_STUDENT".equals(jwt.getClaimAsString("role"))) {
      return Mono.just(ResponseEntity.status(403).build());
    }
    Long studentId = jwt.getClaim("userId");
    List<CourseResponse> dtos = subService
      .getCoursesForStudent(studentId).stream()
      .map(c -> new CourseResponse(
          c.getId(),
          c.getTeacherId(),
          c.getTitle(),
          c.getDescription(),
          c.isApproved()
        ))
      .toList();
    return Mono.just(ResponseEntity.ok(dtos));
  }

  // 3) المعلم يرى المشتركين في دورته

  @GetMapping("/course/{courseId}/subscribers")
  @TimeLimiter(name = "subscribersTimeLimiter",
               fallbackMethod = "subscribersTimeoutFallback")
  public CompletableFuture<List<UserResponse>> subscribers(
      @PathVariable Long courseId,
      Authentication auth
  ) {
    return CompletableFuture.supplyAsync(() -> {
      Jwt jwt = (Jwt) auth.getPrincipal();
      // 1) تأكد دور المكلّم
      if (!"ROLE_TEACHER".equals(jwt.getClaimAsString("role"))) {
        throw new IllegalStateException("Only teachers can view subscribers");
      }
      // 2) تأكد ملكيته للدورة
      var course = courseService.get(courseId);
      if (!course.getTeacherId().equals(jwt.getClaim("userId"))) {
        throw new IllegalStateException("Not your course");
      }
      // 3) استخرج الـ IDs واطلب بيانات كل مستخدم
      String token = jwt.getTokenValue();
      List<Long> studentIds = subService.getStudentIdsForCourse(courseId);

      return studentIds.stream()
        .map(id -> authClient.getUserById(id, token))
        .collect(Collectors.toList());
    });
  }

  // fallback على الـ timeout أو أي استثناء من TimeLimiter
  public CompletableFuture<List<UserResponse>> subscribersTimeoutFallback(
      Long courseId, Authentication auth, Throwable t
  ) {
    // مثلاً نرجّع لائحة فارغة
    return CompletableFuture.completedFuture(List.of());
  }
}
