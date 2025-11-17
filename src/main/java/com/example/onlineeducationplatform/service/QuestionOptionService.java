package com.example.onlineeducationplatform.service;

import java.util.List;

import com.example.onlineeducationplatform.model.QuestionOption;

public interface QuestionOptionService {
    void addQuestionOption(QuestionOption option);
    QuestionOption getQuestionOptionById(Integer id);
    List<QuestionOption> getOptionsByQuestion(Integer questionId);
    List<QuestionOption> getAllOptions();
    void updateQuestionOption(QuestionOption option);
    void deleteQuestionOption(Integer id);
    void deleteOptionsByQuestion(Integer questionId);
    Integer getOptionCountByQuestion(Integer questionId);
}
