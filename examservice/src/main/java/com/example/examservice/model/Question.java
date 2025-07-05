
package com.example.examservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "exam_id", nullable = false)
  @ToString.Exclude @EqualsAndHashCode.Exclude
  private Exam exam;

  @Column(nullable = false, length = 1000)
  private String text;

  @Column(nullable = false)
  private Integer correctAnswer; // 1..4

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude @EqualsAndHashCode.Exclude
  private List<Answer> answers = new ArrayList<>();
}
