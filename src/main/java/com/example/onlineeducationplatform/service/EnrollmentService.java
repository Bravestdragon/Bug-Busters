package com.example.onlineeducationplatform.service;

import java.util.List;

import com.example.onlineeducationplatform.model.Enrollment;

public interface EnrollmentService {
    void enrollStudent(Enrollment enrollment);
    Enrollment getEnrollmentById(Integer id);
    List<Enrollment> getEnrollmentsByCourse(Integer courseId);
    List<Enrollment> getEnrollmentsByStudent(Integer studentId);
    Enrollment checkEnrollment(Integer courseId, Integer studentId);
    List<Enrollment> getAllEnrollments();
    void updateEnrollmentStatus(Integer id, String status);
    void updateProgressPercentage(Integer id, Double progress);
    void dropEnrollment(Integer id);
    void dropEnrollmentByCoursAndStudent(Integer courseId, Integer studentId);
    Integer getEnrollmentCountForCourse(Integer courseId);
}
