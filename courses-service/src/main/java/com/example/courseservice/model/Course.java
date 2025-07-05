package com.example.courseservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "courses")
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private Long teacherId;

  @NotBlank
  private String title;

  @Column(length = 2000)
  private String description;

  @Column(nullable = false)
  private boolean approved = false;


  public Course() {}

  public Long getId() { return id; }

  public Long getTeacherId() { return teacherId; }
  public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public boolean isApproved() { return approved; }
  public void setApproved(boolean approved) { this.approved = approved; }

}
