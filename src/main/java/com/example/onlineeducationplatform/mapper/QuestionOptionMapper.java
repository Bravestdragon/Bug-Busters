package com.example.onlineeducationplatform.mapper;

import java.util.List;

import com.example.onlineeducationplatform.model.QuestionOption;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionOptionMapper {
    
    // Create option
    void insertQuestionOption(QuestionOption option);
    
    // Read options
    QuestionOption selectOptionById(Integer id);
    List<QuestionOption> selectOptionsByQuestion(Integer questionId);
    List<QuestionOption> selectOptionsByQuestionOrdered(Integer questionId);
    List<QuestionOption> selectAllOptions();
    
    // Update option
    void updateQuestionOption(QuestionOption option);
    
    // Delete option
    void deleteQuestionOption(Integer id);
    void deleteOptionsByQuestion(Integer questionId);
    
    // Count
    Integer countOptionsByQuestion(Integer questionId);
}
