package com.example.onlineeducationplatform.service;

import java.util.List;

import com.example.onlineeducationplatform.model.Lesson;

public interface LessonService {
    void addLesson(Lesson lesson);
    Lesson getLessonById(Integer id);
    List<Lesson> getLessonsByCourse(Integer courseId);
    List<Lesson> getLessonsByCourseOrdered(Integer courseId);
    List<Lesson> getPublishedLessonsByCourse(Integer courseId);
    Lesson getLessonByIdAndCourse(Integer id, Integer courseId);
    List<Lesson> getAllLessons();
    void updateLesson(Lesson lesson);
    void updateLessonStatus(Integer id, String status);
    void deleteLesson(Integer id);
    void deleteLessonsByCourse(Integer courseId);
    Integer getLessonCountByCourse(Integer courseId);
}
