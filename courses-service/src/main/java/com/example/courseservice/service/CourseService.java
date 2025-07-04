package com.example.courseservice.service;

import com.example.courseservice.model.Course;
import com.example.courseservice.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
  private final CourseRepository repo;

  public CourseService(CourseRepository repo) {
    this.repo = repo;
  }

  public Course create(Course c) { return repo.save(c); }
  public Course update(Long id, Course c) {
    Course existing = repo.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Course not found"));
    existing.setTeacherId(c.getTeacherId());
    existing.setTitle(c.getTitle());
    existing.setDescription(c.getDescription());
    return repo.save(existing);
  }
  public void delete(Long id) { repo.deleteById(id); }
  public Course get(Long id) {
    return repo.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Course not found"));
  }
  public List<Course> list() { return repo.findAll(); }
 
  public List<Course> forTeacher(Long teacherId) {
    return repo.findByTeacherId(teacherId);
  }

  public Course approve(Long id) {
    Course c = get(id);
    c.setApproved(true);
    return repo.save(c);
  }
}
