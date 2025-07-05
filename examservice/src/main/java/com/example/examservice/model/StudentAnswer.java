
package com.example.examservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnswer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_exam_id", nullable = false)
  @ToString.Exclude @EqualsAndHashCode.Exclude
  private StudentExam studentExam;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id", nullable = false)
  @ToString.Exclude @EqualsAndHashCode.Exclude
  private Question question;

  @Column(nullable = false)
  private Integer selectedOption; // 1..4

  private Boolean correct;
}
