// StudentExam.java
package com.example.examservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student_exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentExam {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long studentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "exam_id", nullable = false)
  @ToString.Exclude @EqualsAndHashCode.Exclude
  private Exam exam;

  @Column(nullable = false)
  private LocalDateTime startedAt;

  private LocalDateTime completedAt;

  private Double score;

  @OneToMany(mappedBy = "studentExam", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude @EqualsAndHashCode.Exclude
  private List<StudentAnswer> studentAnswers = new ArrayList<>();
}
