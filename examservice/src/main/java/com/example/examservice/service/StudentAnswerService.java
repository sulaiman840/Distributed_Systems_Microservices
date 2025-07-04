// StudentAnswerService.java
package com.example.examservice.service;

import com.example.examservice.dto.StudentAnswerRequest;
import com.example.examservice.model.Question;
import com.example.examservice.model.StudentAnswer;
import com.example.examservice.model.StudentExam;
import com.example.examservice.repository.StudentAnswerRepository;
import com.example.examservice.repository.StudentExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentAnswerService {
  private final StudentAnswerRepository studentAnswerRepository;
  private final StudentExamRepository studentExamRepository;
  private final QuestionService questionService;

  public StudentAnswerService(
      StudentAnswerRepository studentAnswerRepository,
      StudentExamRepository studentExamRepository,
      QuestionService questionService
  ) {
    this.studentAnswerRepository = studentAnswerRepository;
    this.studentExamRepository = studentExamRepository;
    this.questionService = questionService;
  }

  public StudentAnswer create(Long studentExamId, StudentAnswerRequest req) {
    StudentExam se = studentExamRepository.findById(studentExamId)
      .orElseThrow(() -> new IllegalArgumentException("StudentExam not found: " + studentExamId));
    Question q = questionService.findById(req.getQuestionId());
    StudentAnswer sa = new StudentAnswer();
    sa.setStudentExam(se);
    sa.setQuestion(q);
    sa.setSelectedOption(req.getSelectedOption());
    return studentAnswerRepository.save(sa);
  }

  public List<StudentAnswer> findByStudentExamId(Long studentExamId) {
    return studentAnswerRepository.findByStudentExamId(studentExamId);
  }

  public List<StudentAnswer> findByQuestionId(Long questionId) {
    return studentAnswerRepository.findByQuestionId(questionId);
  }
}
