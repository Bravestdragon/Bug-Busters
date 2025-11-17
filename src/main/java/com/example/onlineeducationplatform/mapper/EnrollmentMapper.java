package com.example.onlineeducationplatform.mapper;

import java.util.List;

import com.example.onlineeducationplatform.model.Enrollment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EnrollmentMapper {
    
    // Create enrollment
    void insertEnrollment(Enrollment enrollment);
    
    // Read enrollments
    Enrollment selectEnrollmentById(Integer id);
    List<Enrollment> selectEnrollmentsByCourse(Integer courseId);
    List<Enrollment> selectEnrollmentsByStudent(Integer studentId);
    Enrollment selectEnrollmentByCoursAndStudent(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);
    List<Enrollment> selectAllEnrollments();
    
    // Update enrollment
    void updateEnrollment(Enrollment enrollment);
    void updateEnrollmentStatus(@Param("id") Integer id, @Param("status") String status);
    void updateProgressPercentage(@Param("id") Integer id, @Param("progress") Double progress);
    
    // Delete enrollment
    void deleteEnrollment(Integer id);
    void deleteEnrollmentByCoursAndStudent(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);
    
    // Count enrollments
    Integer countEnrollmentsForCourse(Integer courseId);
}
