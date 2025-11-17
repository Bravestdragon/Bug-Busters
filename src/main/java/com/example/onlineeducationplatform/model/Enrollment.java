package com.example.onlineeducationplatform.model;

import java.time.LocalDateTime;

public class Enrollment {
    private Integer id;
    private Integer courseId;
    private Integer studentId;
    private String studentName;
    private String courseName;
    private String enrollmentStatus; // ACTIVE, COMPLETED, DROPPED
    private LocalDateTime enrolledDate;
    private LocalDateTime completedDate;
    private Double progressPercentage;

    // Constructors
    public Enrollment() {}

    public Enrollment(Integer courseId, Integer studentId, String studentName, String courseName) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseName = courseName;
        this.enrollmentStatus = "ACTIVE";
        this.enrolledDate = LocalDateTime.now();
        this.progressPercentage = 0.0;
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

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public LocalDateTime getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(LocalDateTime enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", enrollmentStatus='" + enrollmentStatus + '\'' +
                ", enrolledDate=" + enrolledDate +
                ", completedDate=" + completedDate +
                ", progressPercentage=" + progressPercentage +
                '}';
    }
}
