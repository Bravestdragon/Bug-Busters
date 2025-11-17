package com.example.onlineeducationplatform.service.impl;

import java.util.List;

import com.example.onlineeducationplatform.mapper.QuestionOptionMapper;
import com.example.onlineeducationplatform.model.QuestionOption;
import com.example.onlineeducationplatform.service.QuestionOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionOptionServiceImpl implements QuestionOptionService {

    @Autowired
    private QuestionOptionMapper questionOptionMapper;

    @Override
    public void addQuestionOption(QuestionOption option) {
        questionOptionMapper.insertQuestionOption(option);
    }

    @Override
    public QuestionOption getQuestionOptionById(Integer id) {
        return questionOptionMapper.selectOptionById(id);
    }

    @Override
    public List<QuestionOption> getOptionsByQuestion(Integer questionId) {
        return questionOptionMapper.selectOptionsByQuestionOrdered(questionId);
    }

    @Override
    public List<QuestionOption> getAllOptions() {
        return questionOptionMapper.selectAllOptions();
    }

    @Override
    public void updateQuestionOption(QuestionOption option) {
        questionOptionMapper.updateQuestionOption(option);
    }

    @Override
    public void deleteQuestionOption(Integer id) {
        questionOptionMapper.deleteQuestionOption(id);
    }

    @Override
    public void deleteOptionsByQuestion(Integer questionId) {
        questionOptionMapper.deleteOptionsByQuestion(questionId);
    }

    @Override
    public Integer getOptionCountByQuestion(Integer questionId) {
        return questionOptionMapper.countOptionsByQuestion(questionId);
    }
}
