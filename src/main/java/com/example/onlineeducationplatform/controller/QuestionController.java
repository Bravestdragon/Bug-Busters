package com.example.onlineeducationplatform.controller;

import java.util.List;

import com.example.onlineeducationplatform.config.JwtUtil;
import com.example.onlineeducationplatform.model.Question;
import com.example.onlineeducationplatform.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private JwtUtil jwtUtil;

    // Get questions by quiz
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<?> getQuestionsByQuiz(@PathVariable Integer quizId) {
        try {
            List<Question> questions = questionService.getQuestionsByQuiz(quizId);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch questions: " + e.getMessage());
        }
    }

    // Get single question
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable Integer id) {
        try {
            Question question = questionService.getQuestionById(id);
            if (question != null) {
                return ResponseEntity.ok(question);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch question: " + e.getMessage());
        }
    }

    // Create question (admin only)
    @PostMapping
    public ResponseEntity<?> createQuestion(@RequestBody Question question,
                                            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }

            String requester = jwtUtil.extractUsername(token);
            
            if (!"axzil".equalsIgnoreCase(requester)) {
                return new ResponseEntity<>("Only admin can create questions", HttpStatus.FORBIDDEN);
            }

            questionService.addQuestion(question);
            return new ResponseEntity<>(question, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Update question (admin only)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Integer id,
                                            @RequestBody Question question,
                                            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }

            String requester = jwtUtil.extractUsername(token);
            
            if (!"axzil".equalsIgnoreCase(requester)) {
                return new ResponseEntity<>("Only admin can update questions", HttpStatus.FORBIDDEN);
            }

            question.setId(id);
            questionService.updateQuestion(question);
            return ResponseEntity.ok("Question updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Delete question (admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Integer id,
                                            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }

            String requester = jwtUtil.extractUsername(token);
            
            if (!"axzil".equalsIgnoreCase(requester)) {
                return new ResponseEntity<>("Only admin can delete questions", HttpStatus.FORBIDDEN);
            }

            questionService.deleteQuestion(id);
            return ResponseEntity.ok("Question deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Get question count
    @GetMapping("/quiz/{quizId}/count")
    public ResponseEntity<?> getQuestionCount(@PathVariable Integer quizId) {
        try {
            Integer count = questionService.getQuestionCountByQuiz(quizId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
