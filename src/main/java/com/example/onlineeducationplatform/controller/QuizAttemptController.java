package com.example.onlineeducationplatform.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.example.onlineeducationplatform.config.JwtUtil;
import com.example.onlineeducationplatform.model.QuizAttempt;
import com.example.onlineeducationplatform.model.User;
import com.example.onlineeducationplatform.service.QuizAttemptService;
import com.example.onlineeducationplatform.service.UserService;
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
@RequestMapping("/api/quiz-attempts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QuizAttemptController {

    @Autowired
    private QuizAttemptService quizAttemptService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Start quiz attempt
    @PostMapping
    public ResponseEntity<?> startQuizAttempt(@RequestBody QuizAttempt attempt,
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
            User user = userService.getUserByUsername(requester);
            
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            attempt.setUserId(user.getId());
            attempt.setStartTime(LocalDateTime.now());
            attempt.setStatus("IN_PROGRESS");
            quizAttemptService.startQuizAttempt(attempt);
            return new ResponseEntity<>(attempt, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Submit quiz attempt
    @PutMapping("/{id}/submit")
    public ResponseEntity<?> submitQuizAttempt(@PathVariable Integer id,
                                               @RequestBody QuizAttempt attempt,
                                               @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }

            attempt.setId(id);
            attempt.setEndTime(LocalDateTime.now());
            attempt.setStatus("COMPLETED");
            quizAttemptService.submitQuizAttempt(attempt);
            return ResponseEntity.ok("Quiz submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Get attempt by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAttemptById(@PathVariable Integer id) {
        try {
            QuizAttempt attempt = quizAttemptService.getQuizAttemptById(id);
            if (attempt != null) {
                return ResponseEntity.ok(attempt);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch attempt: " + e.getMessage());
        }
    }

    // Get attempts by quiz
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<?> getAttemptsByQuiz(@PathVariable Integer quizId) {
        try {
            List<QuizAttempt> attempts = quizAttemptService.getAttemptsByQuiz(quizId);
            return ResponseEntity.ok(attempts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch attempts: " + e.getMessage());
        }
    }

    // Get attempts by student
    @GetMapping("/student/my-attempts")
    public ResponseEntity<?> getMyAttempts(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }

            String requester = jwtUtil.extractUsername(token);
            User user = userService.getUserByUsername(requester);
            
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            List<QuizAttempt> attempts = quizAttemptService.getAttemptsByStudent(user.getId());
            return ResponseEntity.ok(attempts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Get attempts for quiz and student
    @GetMapping("/quiz/{quizId}/student")
    public ResponseEntity<?> getAttemptsByQuizAndStudent(@PathVariable Integer quizId,
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
            User user = userService.getUserByUsername(requester);
            
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            List<QuizAttempt> attempts = quizAttemptService.getAttemptsByQuizAndStudent(quizId, user.getId());
            return ResponseEntity.ok(attempts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Delete attempt (admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttempt(@PathVariable Integer id,
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
                return new ResponseEntity<>("Only admin can delete attempts", HttpStatus.FORBIDDEN);
            }

            quizAttemptService.deleteQuizAttempt(id);
            return ResponseEntity.ok("Attempt deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
