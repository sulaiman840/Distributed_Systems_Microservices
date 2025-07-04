// StudentExamRepository.java
package com.example.examservice.repository;

import com.example.examservice.model.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {
    /** All exam‐attempts by a given student */
    List<StudentExam> findByStudentId(Long studentId);

    /** All attempts for a given exam */
    List<StudentExam> findByExamId(Long examId);
}
