// StudentAnswerRepository.java
package com.example.examservice.repository;

import com.example.examservice.model.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    /** All answers submitted in a particular student‐exam session */
    List<StudentAnswer> findByStudentExamId(Long studentExamId);

    /** All student‐answers for a given question (across all students) */
    List<StudentAnswer> findByQuestionId(Long questionId);
}
