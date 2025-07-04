package com.example.examservice.service;

import com.example.examservice.model.Answer;
import com.example.examservice.repository.AnswerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

  private final AnswerRepository answerRepository;
  private final QuestionService questionService;

  public AnswerService(AnswerRepository answerRepository,
                       QuestionService questionService) {
    this.answerRepository = answerRepository;
    this.questionService = questionService;
  }

  public List<Answer> findByQuestionId(Long questionId) {
    return answerRepository.findByQuestionId(questionId);
  }

  public Answer findById(Long id) {
    return answerRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Answer not found: " + id));
  }

  public Answer create(Long questionId, Integer optionIndex, String body) {
    // delegate to QuestionService.createAnswer
    return questionService.createAnswer(questionId, optionIndex, body);
  }

  public Answer update(Long id, Integer optionIndex, String body) {
    Answer a = findById(id);
    a.setOptionIndex(optionIndex);
    a.setBody(body);
    return answerRepository.save(a);
  }

  public void delete(Long id) {
    answerRepository.deleteById(id);
  }
}
