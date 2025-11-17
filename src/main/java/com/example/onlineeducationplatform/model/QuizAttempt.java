package com.example.onlineeducationplatform.model;

import java.time.LocalDateTime;

public class QuizAttempt {
    private Integer id;
    private Integer quizId;
    private Integer enrollmentId;
    private Integer userId;
    private Integer score;
    private Integer totalPoints;
    private Double percentage;
    private String status; // IN_PROGRESS, COMPLETED, SUBMITTED
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer timeSpentSeconds;

    // Constructors
    public QuizAttempt() {}

    public QuizAttempt(Integer quizId, Integer enrollmentId, Integer userId) {
        this.quizId = quizId;
        this.enrollmentId = enrollmentId;
        this.userId = userId;
        this.status = "IN_PROGRESS";
        this.startTime = LocalDateTime.now();
        this.score = 0;
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

    public Integer getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Integer enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public void setTimeSpentSeconds(Integer timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    @Override
    public String toString() {
        return "QuizAttempt{" +
                "id=" + id +
                ", quizId=" + quizId +
                ", enrollmentId=" + enrollmentId +
                ", score=" + score +
                ", percentage=" + percentage +
                ", status='" + status + '\'' +
                '}';
    }
}
