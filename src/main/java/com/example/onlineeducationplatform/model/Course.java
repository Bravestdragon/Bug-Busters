package com.example.onlineeducationplatform.model;

import java.time.LocalDateTime;

public class Course {
    private Integer id;
    private String title;
    private String description;
    private String category;
    private Integer instructorId;
    private String instructorName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String status; // DRAFT, PUBLISHED, ARCHIVED
    private Integer enrollmentCount;

    public Course() {
    }

    public Course(String title, String description, String category, Integer instructorId, String instructorName) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.status = "DRAFT";
        this.createdDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getEnrollmentCount() {
        return enrollmentCount;
    }

    public void setEnrollmentCount(Integer enrollmentCount) {
        this.enrollmentCount = enrollmentCount;
    }
}
