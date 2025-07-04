// StudentExamRequest.java
package com.example.examservice.dto;

public class StudentExamRequest {
    private Long studentId;

    public StudentExamRequest() {}

    public StudentExamRequest(Long studentId) {
        this.studentId = studentId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
