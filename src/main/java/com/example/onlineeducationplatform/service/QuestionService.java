package com.example.onlineeducationplatform.service;

import java.util.List;

import com.example.onlineeducationplatform.model.Question;

public interface QuestionService {
    void addQuestion(Question question);
    Question getQuestionById(Integer id);
    List<Question> getQuestionsByQuiz(Integer quizId);
    List<Question> getAllQuestions();
    void updateQuestion(Question question);
    void deleteQuestion(Integer id);
    void deleteQuestionsByQuiz(Integer quizId);
    Integer getQuestionCountByQuiz(Integer quizId);
}
