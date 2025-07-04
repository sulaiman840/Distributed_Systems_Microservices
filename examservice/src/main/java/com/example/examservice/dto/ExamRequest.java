// ExamRequest.java
package com.example.examservice.dto;

import java.time.LocalDateTime;

public class ExamRequest {
    private Long courseId;
    private String title;
    private LocalDateTime date;

    public ExamRequest() {}

    public ExamRequest(Long courseId, String title, LocalDateTime date) {
        this.courseId = courseId;
        this.title = title;
        this.date = date;
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
