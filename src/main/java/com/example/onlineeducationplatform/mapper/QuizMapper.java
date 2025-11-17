package com.example.onlineeducationplatform.mapper;

import java.util.List;

import com.example.onlineeducationplatform.model.Quiz;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuizMapper {
    
    // Create quiz
    void insertQuiz(Quiz quiz);
    
    // Read quizzes
    Quiz selectQuizById(Integer id);
    List<Quiz> selectQuizzesByCourse(Integer courseId);
    List<Quiz> selectQuizzesByLesson(Integer lessonId);
    List<Quiz> selectPublishedQuizzesByCourse(Integer courseId);
    List<Quiz> selectPublishedQuizzesByLesson(Integer lessonId);
    List<Quiz> selectAllQuizzes();
    
    // Update quiz
    void updateQuiz(Quiz quiz);
    void updateQuizStatus(@Param("id") Integer id, @Param("status") String status);
    void updateQuizTotalQuestions(@Param("id") Integer id, @Param("totalQuestions") Integer totalQuestions);
    
    // Delete quiz
    void deleteQuiz(Integer id);
    void deleteQuizzesByCourse(Integer courseId);
    void deleteQuizzesByLesson(Integer lessonId);
    
    // Count
    Integer countQuizzesByCourse(Integer courseId);
    Integer countQuizzesByLesson(Integer lessonId);
}
