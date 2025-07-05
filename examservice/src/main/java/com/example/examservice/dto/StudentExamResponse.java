
package com.example.examservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public class StudentExamResponse {
    private Long id;
    private Long studentId;
    private Long examId;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Double score;
    private List<StudentAnswerResponse> studentAnswers;

    public StudentExamResponse() {}

    public StudentExamResponse(Long id, Long studentId, Long examId,
                               LocalDateTime startedAt, LocalDateTime completedAt,
                               Double score, List<StudentAnswerResponse> studentAnswers) {
        this.id = id;
        this.studentId = studentId;
        this.examId = examId;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.score = score;
        this.studentAnswers = studentAnswers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public List<StudentAnswerResponse> getStudentAnswers() {
        return studentAnswers;
    }

    public void setStudentAnswers(List<StudentAnswerResponse> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }
}
