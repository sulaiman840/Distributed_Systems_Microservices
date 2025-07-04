package com.example.examservice.service;

import com.example.examservice.model.Exam;
import com.example.examservice.model.StudentAnswer;
import com.example.examservice.model.StudentExam;
import com.example.examservice.repository.StudentExamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentExamService {

  private final StudentExamRepository studentExamRepository;
  private final ExamService examService;
  private final QuestionService questionService;

  public StudentExamService(StudentExamRepository studentExamRepository,
                            ExamService examService,
                            QuestionService questionService) {
    this.studentExamRepository = studentExamRepository;
    this.examService = examService;
    this.questionService = questionService;
  }

  /** Called by controller with (examId, studentId) */
  public StudentExam startExam(Long examId, Long studentId) {
    Exam exam = examService.findById(examId)
      .orElseThrow(() -> new IllegalArgumentException("Exam not found: " + examId));
    StudentExam se = new StudentExam();
    se.setStudentId(studentId);
    se.setExam(exam);
    se.setStartedAt(LocalDateTime.now());
    return studentExamRepository.save(se);
  }

  /**
   * Submit a batch of StudentAnswer entities, compute score, mark completedAt.
   * The controller builds the List<StudentAnswer> with question + selectedOption set.
   */
  public StudentExam submitAnswers(Long examId, Long studentExamId, List<StudentAnswer> answers) {
    StudentExam se = studentExamRepository.findById(studentExamId)
      .orElseThrow(() -> new IllegalArgumentException("StudentExam not found: " + studentExamId));
    se.setCompletedAt(LocalDateTime.now());
    // associate each answer with this StudentExam
    answers.forEach(sa -> {
      sa.setStudentExam(se);
      // compute correct flag
      sa.setCorrect(
        sa.getSelectedOption().equals(sa.getQuestion().getCorrectAnswer())
      );
      se.getStudentAnswers().add(sa);
    });
    // compute score as percentage
    long correctCount = se.getStudentAnswers().stream()
      .filter(StudentAnswer::getCorrect)
      .count();
    double score = se.getStudentAnswers().isEmpty()
      ? 0.0
      : (double) correctCount / se.getStudentAnswers().size() * 100.0;
    se.setScore(score);
    return studentExamRepository.save(se);
  }

  public Optional<StudentExam> getProgress(Long examId, Long studentExamId) {
    return studentExamRepository.findById(studentExamId)
      .filter(se -> se.getExam().getId().equals(examId));
  }
}
