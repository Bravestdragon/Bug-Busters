package com.example.onlineeducationplatform.model;

import java.time.LocalDateTime;

public class Lesson {
    private Integer id;
    private Integer courseId;
    private String title;
    private String description;
    private String content; // HTML or markdown content
    private String videoUrl; // Optional video URL
    private Integer sequenceNumber; // Order in course
    private Integer durationMinutes; // Lesson duration
    private String status; // DRAFT, PUBLISHED, ARCHIVED
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // Constructors
    public Lesson() {}

    public Lesson(Integer courseId, String title, String description, Integer sequenceNumber) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.sequenceNumber = sequenceNumber;
        this.status = "DRAFT";
        this.durationMinutes = 0;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return "Lesson{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", status='" + status + '\'' +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}
