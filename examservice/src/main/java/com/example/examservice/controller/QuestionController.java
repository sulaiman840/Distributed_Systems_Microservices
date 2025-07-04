package com.example.examservice.controller;

import com.example.examservice.dto.AnswerRequest;
import com.example.examservice.dto.AnswerResponse;
import com.example.examservice.dto.QuestionRequest;
import com.example.examservice.dto.QuestionResponse;
import com.example.examservice.model.Answer;
import com.example.examservice.model.Question;
import com.example.examservice.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exams/{examId}/questions")
public class QuestionController {

  private final QuestionService questionService;

  public QuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  @GetMapping
  public ResponseEntity<List<QuestionResponse>> list(
      @PathVariable Long examId,
      Authentication auth
  ) {
    // any authenticated user may list questions
    List<QuestionResponse> dto = questionService
      .findByExam(examId)
      .stream()
      .map(q -> new QuestionResponse(q.getId(), q.getText(), q.getCorrectAnswer()))
      .collect(Collectors.toList());
    return ResponseEntity.ok(dto);
  }

  @PostMapping
  public ResponseEntity<?> create(
      @PathVariable Long examId,
      @RequestBody QuestionRequest req,
      Authentication auth
  ) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    String role = jwt.getClaimAsString("role");
    if (!"ROLE_TEACHER".equals(role)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Only teachers can add questions");
    }

    Question q = questionService.create(examId, req.getText(), req.getCorrectAnswer());
    return ResponseEntity.ok(new QuestionResponse(q.getId(), q.getText(), q.getCorrectAnswer()));
  }

  @GetMapping("/{questionId}/answers")
  public ResponseEntity<List<AnswerResponse>> listAnswers(
      @PathVariable Long examId,
      @PathVariable Long questionId,
      Authentication auth
  ) {
    // any authenticated user may list answers
    List<AnswerResponse> dto = questionService
      .findAnswers(questionId)
      .stream()
      .map(a -> new AnswerResponse(a.getId(), a.getOptionIndex(), a.getBody()))
      .collect(Collectors.toList());
    return ResponseEntity.ok(dto);
  }

  @PostMapping("/{questionId}/answers")
  public ResponseEntity<?> createAnswer(
      @PathVariable Long examId,
      @PathVariable Long questionId,
      @RequestBody AnswerRequest req,
      Authentication auth
  ) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    String role = jwt.getClaimAsString("role");
    if (!"ROLE_TEACHER".equals(role)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Only teachers can add answers");
    }

    Answer a = questionService.createAnswer(questionId, req.getOptionIndex(), req.getBody());
    return ResponseEntity.ok(new AnswerResponse(a.getId(), a.getOptionIndex(), a.getBody()));
  }
}
