package com.example.onlineeducationplatform.service.impl;

import java.util.List;

import com.example.onlineeducationplatform.mapper.QuizMapper;
import com.example.onlineeducationplatform.model.Quiz;
import com.example.onlineeducationplatform.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizMapper quizMapper;

    @Override
    public void addQuiz(Quiz quiz) {
        quizMapper.insertQuiz(quiz);
    }

    @Override
    public Quiz getQuizById(Integer id) {
        return quizMapper.selectQuizById(id);
    }

    @Override
    public List<Quiz> getQuizzesByCourse(Integer courseId) {
        return quizMapper.selectQuizzesByCourse(courseId);
    }

    @Override
    public List<Quiz> getPublishedQuizzesByCourse(Integer courseId) {
        return quizMapper.selectPublishedQuizzesByCourse(courseId);
    }

    @Override
    public List<Quiz> getQuizzesByLesson(Integer lessonId) {
        return quizMapper.selectQuizzesByLesson(lessonId);
    }

    @Override
    public List<Quiz> getPublishedQuizzesByLesson(Integer lessonId) {
        return quizMapper.selectPublishedQuizzesByLesson(lessonId);
    }

    @Override
    public List<Quiz> getAllQuizzes() {
        return quizMapper.selectAllQuizzes();
    }

    @Override
    public void updateQuiz(Quiz quiz) {
        quizMapper.updateQuiz(quiz);
    }

    @Override
    public void updateQuizStatus(Integer id, String status) {
        quizMapper.updateQuizStatus(id, status);
    }

    @Override
    public void deleteQuiz(Integer id) {
        quizMapper.deleteQuiz(id);
    }

    @Override
    public void deleteQuizzesByCourse(Integer courseId) {
        quizMapper.deleteQuizzesByCourse(courseId);
    }

    @Override
    public Integer getQuizCountByCourse(Integer courseId) {
        return quizMapper.countQuizzesByCourse(courseId);
    }

    @Override
    public Integer getQuizCountByLesson(Integer lessonId) {
        return quizMapper.countQuizzesByLesson(lessonId);
    }
}
