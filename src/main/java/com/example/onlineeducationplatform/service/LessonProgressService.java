package com.example.onlineeducationplatform.service;

import java.util.List;

import com.example.onlineeducationplatform.model.LessonProgress;

public interface LessonProgressService {
    void createProgress(LessonProgress progress);
    LessonProgress getProgressById(Integer id);
    LessonProgress getProgress(Integer enrollmentId, Integer lessonId);
    List<LessonProgress> getProgressByEnrollment(Integer enrollmentId);
    List<LessonProgress> getProgressByLesson(Integer lessonId);
    List<LessonProgress> getProgressByStudent(Integer studentId);
    List<LessonProgress> getProgressByCourse(Integer courseId);
    void updateProgress(LessonProgress progress);
    void markLessonComplete(Integer enrollmentId, Integer lessonId);
    void updateTimeSpent(Integer enrollmentId, Integer lessonId, Integer minutes);
    void deleteProgress(Integer id);
    Integer getCompletedLessonCount(Integer enrollmentId);
    Integer getCompletedLessonCountByCourse(Integer enrollmentId, Integer courseId);
}
