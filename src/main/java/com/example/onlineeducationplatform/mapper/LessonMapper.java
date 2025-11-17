package com.example.onlineeducationplatform.mapper;

import java.util.List;

import com.example.onlineeducationplatform.model.Lesson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LessonMapper {
    
    // Create lesson
    void insertLesson(Lesson lesson);
    
    // Read lessons
    Lesson selectLessonById(Integer id);
    List<Lesson> selectLessonsByCourse(Integer courseId);
    List<Lesson> selectLessonsByCourseOrdered(Integer courseId);
    List<Lesson> selectPublishedLessonsByCourse(Integer courseId);
    Lesson selectLessonByIdAndCourse(@Param("id") Integer id, @Param("courseId") Integer courseId);
    List<Lesson> selectAllLessons();
    
    // Update lesson
    void updateLesson(Lesson lesson);
    void updateLessonStatus(@Param("id") Integer id, @Param("status") String status);
    
    // Delete lesson
    void deleteLesson(Integer id);
    void deleteLessonsByCourse(Integer courseId);
    
    // Count
    Integer countLessonsByCourse(Integer courseId);
}
