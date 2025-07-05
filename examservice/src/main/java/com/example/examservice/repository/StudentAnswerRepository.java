
package com.example.examservice.repository;

import com.example.examservice.model.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
   
    List<StudentAnswer> findByStudentExamId(Long studentExamId);

   
    List<StudentAnswer> findByQuestionId(Long questionId);
}
