package com.example.onlineeducationplatform.model;

import java.time.LocalDateTime;

public class Assignment {
    private Long id;
    private Long courseId;
    private Long lessonId;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Integer maxScore;
    private String requirements;
    private String status; // ACTIVE, CLOSED, ARCHIVED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Assignment() {}

    public Assignment(Long courseId, Long lessonId, String title, String description, 
                     LocalDateTime dueDate, Integer maxScore, String requirements, String status) {
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.maxScore = maxScore;
        this.requirements = requirements;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public Integer getMaxScore() { return maxScore; }
    public void setMaxScore(Integer maxScore) { this.maxScore = maxScore; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
