// ExamRepository.java
package com.example.examservice.repository;

import com.example.examservice.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    /** Find all exams for a given course */
    List<Exam> findByCourseId(Long courseId);
}
