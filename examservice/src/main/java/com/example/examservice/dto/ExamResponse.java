// ExamResponse.java
package com.example.examservice.dto;

import java.time.LocalDateTime;

public class ExamResponse {
    private Long id;
    private Long courseId;
    private String title;
    private LocalDateTime date;

    public ExamResponse() {}

    public ExamResponse(Long id, Long courseId, String title, LocalDateTime date) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
