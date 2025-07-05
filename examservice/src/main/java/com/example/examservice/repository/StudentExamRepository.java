
package com.example.examservice.repository;

import com.example.examservice.model.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {
    
    List<StudentExam> findByStudentId(Long studentId);


    List<StudentExam> findByExamId(Long examId);
}
