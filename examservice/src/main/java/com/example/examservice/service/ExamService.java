package com.example.examservice.service;

import com.example.examservice.dto.ExamRequest;
import com.example.examservice.model.Exam;
import com.example.examservice.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

  private final ExamRepository examRepository;

  public ExamService(ExamRepository examRepository) {
    this.examRepository = examRepository;
  }

  public List<Exam> findAll() {
    return examRepository.findAll();
  }

  public Optional<Exam> findById(Long id) {
    return examRepository.findById(id);
  }

  public Exam create(ExamRequest req) {
    Exam e = new Exam();
    e.setCourseId(req.getCourseId());
    e.setTitle(req.getTitle());
    e.setDate(req.getDate());
    return examRepository.save(e);
  }

  public Optional<Exam> update(Long id, ExamRequest req) {
    return examRepository.findById(id)
      .map(existing -> {
        existing.setCourseId(req.getCourseId());
        existing.setTitle(req.getTitle());
        existing.setDate(req.getDate());
        return examRepository.save(existing);
      });
  }

  public boolean delete(Long id) {
    if (!examRepository.existsById(id)) {
      return false;
    }
    examRepository.deleteById(id);
    return true;
  }
}
