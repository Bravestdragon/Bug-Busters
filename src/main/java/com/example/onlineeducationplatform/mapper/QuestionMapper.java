package com.example.onlineeducationplatform.mapper;

import java.util.List;

import com.example.onlineeducationplatform.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuestionMapper {
    
    // Create question
    void insertQuestion(Question question);
    
    // Read questions
    Question selectQuestionById(Integer id);
    List<Question> selectQuestionsByQuiz(Integer quizId);
    List<Question> selectQuestionsByQuizOrdered(Integer quizId);
    List<Question> selectAllQuestions();
    
    // Update question
    void updateQuestion(Question question);
    void updateQuestionSequence(@Param("id") Integer id, @Param("sequenceNumber") Integer sequenceNumber);
    
    // Delete question
    void deleteQuestion(Integer id);
    void deleteQuestionsByQuiz(Integer quizId);
    
    // Count
    Integer countQuestionsByQuiz(Integer quizId);
}
