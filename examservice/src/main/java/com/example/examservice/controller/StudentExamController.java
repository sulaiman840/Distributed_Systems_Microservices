package com.example.examservice.controller;

import com.example.examservice.dto.StudentAnswerRequest;
import com.example.examservice.dto.StudentAnswerResponse;
import com.example.examservice.dto.StudentExamRequest;
import com.example.examservice.dto.StudentExamResponse;
import com.example.examservice.model.StudentAnswer;
import com.example.examservice.model.StudentExam;
import com.example.examservice.service.QuestionService;
import com.example.examservice.service.StudentExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exams/{examId}/student-exams")
public class StudentExamController {

  private final StudentExamService studentExamService;
  private final QuestionService questionService;

  public StudentExamController(
      StudentExamService studentExamService,
      QuestionService questionService
  ) {
    this.studentExamService = studentExamService;
    this.questionService = questionService;
  }

  @PostMapping("/start")
  public ResponseEntity<?> start(
      @PathVariable Long examId,
      @RequestBody StudentExamRequest req,
      Authentication auth
  ) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    String role = jwt.getClaimAsString("role");
    if (!"ROLE_STUDENT".equals(role)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Only students can start an exam");
    }


    Long studentId = jwt.getClaim("userId");
    StudentExam se = studentExamService.startExam(examId, studentId);
    return ResponseEntity.ok(toDto(se));
  }

  @PostMapping("/{studentExamId}/submit")
  public ResponseEntity<?> submit(
      @PathVariable Long examId,
      @PathVariable Long studentExamId,
      @RequestBody List<StudentAnswerRequest> answers,
      Authentication auth
  ) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    String role = jwt.getClaimAsString("role");
    if (!"ROLE_STUDENT".equals(role)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Only students can submit answers");
    }

    
    List<StudentAnswer> domain =
      answers.stream()
        .map(r -> {
          StudentAnswer a = new StudentAnswer();
          a.setQuestion(questionService.findById(r.getQuestionId()));
          a.setSelectedOption(r.getSelectedOption());
          return a;
        })
        .collect(Collectors.toList());

    StudentExam se = studentExamService.submitAnswers(examId, studentExamId, domain);
    return ResponseEntity.ok(toDto(se));
  }

  @GetMapping("/{studentExamId}/progress")
  public ResponseEntity<?> progress(
      @PathVariable Long examId,
      @PathVariable Long studentExamId,
      Authentication auth
  ) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    String role = jwt.getClaimAsString("role");
    if (!"ROLE_STUDENT".equals(role)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Only students can view their progress");
    }

    return studentExamService.getProgress(examId, studentExamId)
      .map(this::toDto)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  private StudentExamResponse toDto(StudentExam se) {
    List<StudentAnswerResponse> ans = se.getStudentAnswers().stream()
      .map(a -> new StudentAnswerResponse(
        a.getId(),
        a.getQuestion().getId(),
        a.getSelectedOption(),
        a.getCorrect()
      ))
      .collect(Collectors.toList());

    return new StudentExamResponse(
      se.getId(),
      se.getStudentId(),
      se.getExam().getId(),
      se.getStartedAt(),
      se.getCompletedAt(),
      se.getScore(),
      ans
    );
  }
}
