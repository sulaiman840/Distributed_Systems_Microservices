
package com.example.examservice.dto;

public class StudentAnswerResponse {
    private Long id;
    private Long questionId;
    private Integer selectedOption;
    private Boolean correct;

    public StudentAnswerResponse() {}

    public StudentAnswerResponse(Long id, Long questionId, Integer selectedOption, Boolean correct) {
        this.id = id;
        this.questionId = questionId;
        this.selectedOption = selectedOption;
        this.correct = correct;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
