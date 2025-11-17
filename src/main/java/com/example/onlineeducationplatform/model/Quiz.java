package com.example.onlineeducationplatform.model;

import java.time.LocalDateTime;

public class Quiz {
    private Integer id;
    private Integer courseId;
    private Integer lessonId;
    private String title;
    private String description;
    private Integer passingScore; // Percentage needed to pass
    private Integer timeLimit; // Minutes allowed to complete
    private Integer totalQuestions;
    private String status; // DRAFT, PUBLISHED, ARCHIVED
    private Integer createdBy; // User ID of instructor
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // Constructors
    public Quiz() {}

    public Quiz(Integer courseId, Integer lessonId, String title, Integer passingScore) {
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.title = title;
        this.passingScore = passingScore;
        this.status = "DRAFT";
        this.totalQuestions = 0;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(Integer passingScore) {
        this.passingScore = passingScore;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
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
        return "Quiz{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", lessonId=" + lessonId +
                ", title='" + title + '\'' +
                ", passingScore=" + passingScore +
                ", totalQuestions=" + totalQuestions +
                ", status='" + status + '\'' +
                '}';
    }
}
