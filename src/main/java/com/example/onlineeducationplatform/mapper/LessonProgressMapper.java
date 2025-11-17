package com.example.onlineeducationplatform.mapper;

import java.util.List;

import com.example.onlineeducationplatform.model.LessonProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LessonProgressMapper {
    
    // Create progress
    void insertProgress(LessonProgress progress);
    
    // Read progress
    LessonProgress selectProgressById(Integer id);
    LessonProgress selectProgressByEnrollmentAndLesson(@Param("enrollmentId") Integer enrollmentId, @Param("lessonId") Integer lessonId);
    List<LessonProgress> selectProgressByEnrollment(Integer enrollmentId);
    List<LessonProgress> selectProgressByLesson(Integer lessonId);
    List<LessonProgress> selectProgressByStudent(Integer studentId);
    List<LessonProgress> selectProgressByCourse(Integer courseId);
    
    // Update progress
    void updateProgress(LessonProgress progress);
    void markLessonComplete(@Param("enrollmentId") Integer enrollmentId, @Param("lessonId") Integer lessonId);
    void updateTimeSpent(@Param("enrollmentId") Integer enrollmentId, @Param("lessonId") Integer lessonId, @Param("minutes") Integer minutes);
    
    // Delete progress
    void deleteProgress(Integer id);
    
    // Count
    Integer countCompletedLessons(Integer enrollmentId);
    Integer countCompletedLessonsByCourse(@Param("enrollmentId") Integer enrollmentId, @Param("courseId") Integer courseId);
}
