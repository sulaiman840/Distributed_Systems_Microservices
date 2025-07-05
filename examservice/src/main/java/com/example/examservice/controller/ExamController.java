package com.example.examservice.controller;

import com.example.examservice.dto.ExamRequest;
import com.example.examservice.dto.ExamResponse;
import com.example.examservice.model.Exam;
import com.example.examservice.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exams")
public class ExamController {

  private final ExamService examService;

  public ExamController(ExamService examService) {
    this.examService = examService;
  }

  @GetMapping
  public ResponseEntity<List<ExamResponse>> getAll(Authentication auth) {
   
    List<ExamResponse> dto = examService.findAll().stream()
      .map(this::toDto)
      .collect(Collectors.toList());
    return ResponseEntity.ok(dto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ExamResponse> getById(
      @PathVariable Long id,
      Authentication auth
  ) {
  
    return examService.findById(id)
      .map(this::toDto)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<?> create(
      @RequestBody ExamRequest req,
      Authentication auth
  ) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    String role = jwt.getClaimAsString("role");
    if (!"ROLE_TEACHER".equals(role)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Only teachers can create exams");
    }

    Exam created = examService.create(req);
    return ResponseEntity.ok(toDto(created));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(
      @PathVariable Long id,
      @RequestBody ExamRequest req,
      Authentication auth
  ) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    String role = jwt.getClaimAsString("role");
    if (!"ROLE_TEACHER".equals(role)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Only teachers can update exams");
    }

    return examService.update(id, req)
      .map(this::toDto)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(
      @PathVariable Long id,
      Authentication auth
  ) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    String role = jwt.getClaimAsString("role");
    if (!"ROLE_TEACHER".equals(role)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Only teachers can delete exams");
    }

    boolean removed = examService.delete(id);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }

  private ExamResponse toDto(Exam e) {
    return new ExamResponse(
      e.getId(),
      e.getCourseId(),
      e.getTitle(),
      e.getDate()
    );
  }
}
