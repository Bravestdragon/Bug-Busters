package com.example.onlineeducationplatform.model;

public class QuestionOption {
    private Integer id;
    private Integer questionId;
    private String optionText;
    private String optionLabel; // A, B, C, D
    private Integer sequenceNumber;
    private Boolean isCorrect;

    // Constructors
    public QuestionOption() {}

    public QuestionOption(Integer questionId, String optionText, String optionLabel) {
        this.questionId = questionId;
        this.optionText = optionText;
        this.optionLabel = optionLabel;
        this.isCorrect = false;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return "QuestionOption{" +
                "id=" + id +
                ", questionId=" + questionId +
                ", optionLabel='" + optionLabel + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
