// QuestionRequest.java
package com.example.examservice.dto;

public class QuestionRequest {
    private String text;
    private Integer correctAnswer;   // 1..4

    public QuestionRequest() {}

    public QuestionRequest(String text, Integer correctAnswer) {
        this.text = text;
        this.correctAnswer = correctAnswer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
