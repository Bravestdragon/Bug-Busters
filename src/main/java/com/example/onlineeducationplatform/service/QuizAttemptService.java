package com.example.onlineeducationplatform.service;

import java.util.List;

import com.example.onlineeducationplatform.model.QuizAttempt;

public interface QuizAttemptService {
    void startQuizAttempt(QuizAttempt attempt);
    QuizAttempt getQuizAttemptById(Integer id);
    List<QuizAttempt> getAttemptsByQuiz(Integer quizId);
    List<QuizAttempt> getAttemptsByStudent(Integer userId);
    List<QuizAttempt> getAttemptsByEnrollment(Integer enrollmentId);
    List<QuizAttempt> getAttemptsByQuizAndStudent(Integer quizId, Integer userId);
    List<QuizAttempt> getAllAttempts();
    void submitQuizAttempt(QuizAttempt attempt);
    void updateAttemptStatus(Integer id, String status);
    void updateAttemptScore(Integer id, Integer score, Double percentage);
    void deleteQuizAttempt(Integer id);
    Integer getAttemptCountByQuiz(Integer quizId);
    Integer getPassedAttemptCount(Integer quizId, Integer userId, Integer passingScore);
}
