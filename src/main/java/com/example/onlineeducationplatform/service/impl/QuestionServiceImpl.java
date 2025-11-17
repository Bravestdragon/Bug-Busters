package com.example.onlineeducationplatform.service.impl;

import java.util.List;

import com.example.onlineeducationplatform.mapper.QuestionMapper;
import com.example.onlineeducationplatform.model.Question;
import com.example.onlineeducationplatform.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public void addQuestion(Question question) {
        questionMapper.insertQuestion(question);
    }

    @Override
    public Question getQuestionById(Integer id) {
        return questionMapper.selectQuestionById(id);
    }

    @Override
    public List<Question> getQuestionsByQuiz(Integer quizId) {
        return questionMapper.selectQuestionsByQuizOrdered(quizId);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionMapper.selectAllQuestions();
    }

    @Override
    public void updateQuestion(Question question) {
        questionMapper.updateQuestion(question);
    }

    @Override
    public void deleteQuestion(Integer id) {
        questionMapper.deleteQuestion(id);
    }

    @Override
    public void deleteQuestionsByQuiz(Integer quizId) {
        questionMapper.deleteQuestionsByQuiz(quizId);
    }

    @Override
    public Integer getQuestionCountByQuiz(Integer quizId) {
        return questionMapper.countQuestionsByQuiz(quizId);
    }
}
