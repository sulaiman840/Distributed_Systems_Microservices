package com.example.examservice.repository;

import com.example.examservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
  List<Question> findByExamId(Long examId);
}
