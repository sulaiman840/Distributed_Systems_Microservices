// AnswerRequest.java
package com.example.examservice.dto;

public class AnswerRequest {
    private Integer optionIndex;  // 1..4
    private String body;

    public AnswerRequest() {}

    public AnswerRequest(Integer optionIndex, String body) {
        this.optionIndex = optionIndex;
        this.body = body;
    }

    public Integer getOptionIndex() {
        return optionIndex;
    }

    public void setOptionIndex(Integer optionIndex) {
        this.optionIndex = optionIndex;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
