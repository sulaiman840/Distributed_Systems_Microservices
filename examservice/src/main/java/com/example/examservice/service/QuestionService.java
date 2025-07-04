package com.example.examservice.service;

import com.example.examservice.model.Answer;
import com.example.examservice.model.Exam;
import com.example.examservice.model.Question;
import com.example.examservice.repository.AnswerRepository;
import com.example.examservice.repository.ExamRepository;
import com.example.examservice.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

  private final ExamRepository examRepository;
  private final QuestionRepository questionRepository;
  private final AnswerRepository answerRepository;

  public QuestionService(ExamRepository examRepository,
                         QuestionRepository questionRepository,
                         AnswerRepository answerRepository) {
    this.examRepository = examRepository;
    this.questionRepository = questionRepository;
    this.answerRepository = answerRepository;
  }

  public List<Question> findByExam(Long examId) {
    if (!examRepository.existsById(examId)) {
      throw new IllegalArgumentException("Exam not found: " + examId);
    }
    return questionRepository.findByExamId(examId);
  }

  public Question create(Long examId, String text, Integer correctAnswer) {
    Exam exam = examRepository.findById(examId)
      .orElseThrow(() -> new IllegalArgumentException("Exam not found: " + examId));
    Question q = new Question();
    q.setExam(exam);
    q.setText(text);
    q.setCorrectAnswer(correctAnswer);
    return questionRepository.save(q);
  }

  public Question findById(Long questionId) {
    return questionRepository.findById(questionId)
      .orElseThrow(() -> new IllegalArgumentException("Question not found: " + questionId));
  }

  public List<Answer> findAnswers(Long questionId) {
    if (!questionRepository.existsById(questionId)) {
      throw new IllegalArgumentException("Question not found: " + questionId);
    }
    return answerRepository.findByQuestionId(questionId);
  }

  public Answer createAnswer(Long questionId, Integer optionIndex, String body) {
    Question q = findById(questionId);
    Answer a = new Answer();
    a.setQuestion(q);
    a.setOptionIndex(optionIndex);
    a.setBody(body);
    return answerRepository.save(a);
  }
}
