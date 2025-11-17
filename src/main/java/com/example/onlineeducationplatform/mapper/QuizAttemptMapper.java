package com.example.onlineeducationplatform.mapper;

import java.util.List;

import com.example.onlineeducationplatform.model.QuizAttempt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuizAttemptMapper {
    
    // Create attempt
    void insertQuizAttempt(QuizAttempt attempt);
    
    // Read attempts
    QuizAttempt selectQuizAttemptById(Integer id);
    List<QuizAttempt> selectAttemptsByQuiz(Integer quizId);
    List<QuizAttempt> selectAttemptsByStudent(@Param("userId") Integer userId);
    List<QuizAttempt> selectAttemptsByEnrollment(Integer enrollmentId);
    List<QuizAttempt> selectAttemptsByQuizAndStudent(@Param("quizId") Integer quizId, @Param("userId") Integer userId);
    List<QuizAttempt> selectAllAttempts();
    
    // Update attempt
    void updateQuizAttempt(QuizAttempt attempt);
    void updateAttemptStatus(@Param("id") Integer id, @Param("status") String status);
    void updateAttemptScore(@Param("id") Integer id, @Param("score") Integer score, @Param("percentage") Double percentage);
    
    // Delete attempt
    void deleteQuizAttempt(Integer id);
    void deleteAttemptsByQuiz(Integer quizId);
    void deleteAttemptsByStudent(Integer userId);
    
    // Count
    Integer countAttemptsByQuiz(Integer quizId);
    Integer countAttemptsByStudent(Integer userId);
    Integer countPassedAttempts(@Param("quizId") Integer quizId, @Param("userId") Integer userId, @Param("passingScore") Integer passingScore);
}
