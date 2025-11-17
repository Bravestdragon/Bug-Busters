package com.example.onlineeducationplatform.model;

import java.time.LocalDateTime;

public class LessonProgress {
    private Integer id;
    private Integer enrollmentId;
    private Integer lessonId;
    private Integer courseId;
    private Integer studentId;
    private Boolean isCompleted;
    private LocalDateTime completedDate;
    private Integer timeSpentMinutes;
    private LocalDateTime lastAccessedDate;

    // Constructors
    public LessonProgress() {}

    public LessonProgress(Integer enrollmentId, Integer lessonId, Integer courseId, Integer studentId) {
        this.enrollmentId = enrollmentId;
        this.lessonId = lessonId;
        this.courseId = courseId;
        this.studentId = studentId;
        this.isCompleted = false;
        this.timeSpentMinutes = 0;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Integer enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public Integer getTimeSpentMinutes() {
        return timeSpentMinutes;
    }

    public void setTimeSpentMinutes(Integer timeSpentMinutes) {
        this.timeSpentMinutes = timeSpentMinutes;
    }

    public LocalDateTime getLastAccessedDate() {
        return lastAccessedDate;
    }

    public void setLastAccessedDate(LocalDateTime lastAccessedDate) {
        this.lastAccessedDate = lastAccessedDate;
    }

    @Override
    public String toString() {
        return "LessonProgress{" +
                "id=" + id +
                ", enrollmentId=" + enrollmentId +
                ", lessonId=" + lessonId +
                ", isCompleted=" + isCompleted +
                ", timeSpentMinutes=" + timeSpentMinutes +
                '}';
    }
}
