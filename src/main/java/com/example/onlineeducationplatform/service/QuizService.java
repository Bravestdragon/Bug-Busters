package com.example.onlineeducationplatform.service;

import java.util.List;

import com.example.onlineeducationplatform.model.Quiz;

public interface QuizService {
    void addQuiz(Quiz quiz);
    Quiz getQuizById(Integer id);
    List<Quiz> getQuizzesByCourse(Integer courseId);
    List<Quiz> getPublishedQuizzesByCourse(Integer courseId);
    List<Quiz> getQuizzesByLesson(Integer lessonId);
    List<Quiz> getPublishedQuizzesByLesson(Integer lessonId);
    List<Quiz> getAllQuizzes();
    void updateQuiz(Quiz quiz);
    void updateQuizStatus(Integer id, String status);
    void deleteQuiz(Integer id);
    void deleteQuizzesByCourse(Integer courseId);
    Integer getQuizCountByCourse(Integer courseId);
    Integer getQuizCountByLesson(Integer lessonId);
}
