package com.example.courseservice.repository;

import com.example.courseservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
  List<Course> findByTeacherId(Long teacherId);
}
