// StudentAnswerRequest.java
package com.example.examservice.dto;

public class StudentAnswerRequest {
    private Long questionId;
    private Integer selectedOption;  // 1..4

    public StudentAnswerRequest() {}

    public StudentAnswerRequest(Long questionId, Integer selectedOption) {
        this.questionId = questionId;
        this.selectedOption = selectedOption;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Integer getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Integer selectedOption) {
        this.selectedOption = selectedOption;
    }
}
