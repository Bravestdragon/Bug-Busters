package com.example.onlineeducationplatform.service.impl;

import java.util.List;

import com.example.onlineeducationplatform.mapper.EnrollmentMapper;
import com.example.onlineeducationplatform.model.Enrollment;
import com.example.onlineeducationplatform.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Override
    public void enrollStudent(Enrollment enrollment) {
        enrollmentMapper.insertEnrollment(enrollment);
    }

    @Override
    public Enrollment getEnrollmentById(Integer id) {
        return enrollmentMapper.selectEnrollmentById(id);
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourse(Integer courseId) {
        return enrollmentMapper.selectEnrollmentsByCourse(courseId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudent(Integer studentId) {
        return enrollmentMapper.selectEnrollmentsByStudent(studentId);
    }

    @Override
    public Enrollment checkEnrollment(Integer courseId, Integer studentId) {
        return enrollmentMapper.selectEnrollmentByCoursAndStudent(courseId, studentId);
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentMapper.selectAllEnrollments();
    }

    @Override
    public void updateEnrollmentStatus(Integer id, String status) {
        enrollmentMapper.updateEnrollmentStatus(id, status);
    }

    @Override
    public void updateProgressPercentage(Integer id, Double progress) {
        enrollmentMapper.updateProgressPercentage(id, progress);
    }

    @Override
    public void dropEnrollment(Integer id) {
        enrollmentMapper.deleteEnrollment(id);
    }

    @Override
    public void dropEnrollmentByCoursAndStudent(Integer courseId, Integer studentId) {
        enrollmentMapper.deleteEnrollmentByCoursAndStudent(courseId, studentId);
    }

    @Override
    public Integer getEnrollmentCountForCourse(Integer courseId) {
        return enrollmentMapper.countEnrollmentsForCourse(courseId);
    }
}
