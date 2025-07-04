// AnswerResponse.java
package com.example.examservice.dto;

public class AnswerResponse {
    private Long id;
    private Integer optionIndex;
    private String body;

    public AnswerResponse() {}

    public AnswerResponse(Long id, Integer optionIndex, String body) {
        this.id = id;
        this.optionIndex = optionIndex;
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
