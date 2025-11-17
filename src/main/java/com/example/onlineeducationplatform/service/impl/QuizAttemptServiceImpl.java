package com.example.onlineeducationplatform.service.impl;

import java.util.List;

import com.example.onlineeducationplatform.mapper.QuizAttemptMapper;
import com.example.onlineeducationplatform.model.QuizAttempt;
import com.example.onlineeducationplatform.service.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizAttemptServiceImpl implements QuizAttemptService {

    @Autowired
    private QuizAttemptMapper quizAttemptMapper;

    @Override
    public void startQuizAttempt(QuizAttempt attempt) {
        quizAttemptMapper.insertQuizAttempt(attempt);
    }

    @Override
    public QuizAttempt getQuizAttemptById(Integer id) {
        return quizAttemptMapper.selectQuizAttemptById(id);
    }

    @Override
    public List<QuizAttempt> getAttemptsByQuiz(Integer quizId) {
        return quizAttemptMapper.selectAttemptsByQuiz(quizId);
    }

    @Override
    public List<QuizAttempt> getAttemptsByStudent(Integer userId) {
        return quizAttemptMapper.selectAttemptsByStudent(userId);
    }

    @Override
    public List<QuizAttempt> getAttemptsByEnrollment(Integer enrollmentId) {
        return quizAttemptMapper.selectAttemptsByEnrollment(enrollmentId);
    }

    @Override
    public List<QuizAttempt> getAttemptsByQuizAndStudent(Integer quizId, Integer userId) {
        return quizAttemptMapper.selectAttemptsByQuizAndStudent(quizId, userId);
    }

    @Override
    public List<QuizAttempt> getAllAttempts() {
        return quizAttemptMapper.selectAllAttempts();
    }

    @Override
    public void submitQuizAttempt(QuizAttempt attempt) {
        quizAttemptMapper.updateQuizAttempt(attempt);
    }

    @Override
    public void updateAttemptStatus(Integer id, String status) {
        quizAttemptMapper.updateAttemptStatus(id, status);
    }

    @Override
    public void updateAttemptScore(Integer id, Integer score, Double percentage) {
        quizAttemptMapper.updateAttemptScore(id, score, percentage);
    }

    @Override
    public void deleteQuizAttempt(Integer id) {
        quizAttemptMapper.deleteQuizAttempt(id);
    }

    @Override
    public Integer getAttemptCountByQuiz(Integer quizId) {
        return quizAttemptMapper.countAttemptsByQuiz(quizId);
    }

    @Override
    public Integer getPassedAttemptCount(Integer quizId, Integer userId, Integer passingScore) {
        return quizAttemptMapper.countPassedAttempts(quizId, userId, passingScore);
    }
}
