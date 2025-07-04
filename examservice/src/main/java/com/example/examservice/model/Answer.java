// Answer.java
package com.example.examservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id", nullable = false)
  @ToString.Exclude @EqualsAndHashCode.Exclude
  private Question question;

  @Column(nullable = false)
  private Integer optionIndex; // 1..4

  @Column(nullable = false, length = 1000)
  private String body;
}
