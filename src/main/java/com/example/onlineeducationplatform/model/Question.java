package com.example.onlineeducationplatform.model;

import java.time.LocalDateTime;

public class Question {
    private Integer id;
    private Integer quizId;
    private String questionText;
    private String questionType; // MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER, ESSAY
    private Integer points;
    private Integer sequenceNumber;
    private String correctAnswer; // For multiple choice, stores the correct option
    private String explanation; // Explanation of the correct answer
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // Constructors
    public Question() {}

    public Question(Integer quizId, String questionText, String questionType, Integer points) {
        this.quizId = quizId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.points = points;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", quizId=" + quizId +
                ", questionText='" + questionText + '\'' +
                ", questionType='" + questionType + '\'' +
                ", points=" + points +
                '}';
    }
}
