package com.example.courseservice.model;

import jakarta.persistence.*;

@Entity
@Table(
  name = "subscriptions",
  uniqueConstraints = @UniqueConstraint(columnNames = {"course_id","student_id"})
)
public class Subscription {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="course_id",   nullable = false)
  private Long courseId;

  @Column(name="student_id", nullable = false)
  private Long studentId;

  public Subscription() {}

  public Long getId() { return id; }
  public Long getCourseId() { return courseId; }
  public void setCourseId(Long courseId) { this.courseId = courseId; }
  public Long getStudentId() { return studentId; }
  public void setStudentId(Long studentId) { this.studentId = studentId; }
}
