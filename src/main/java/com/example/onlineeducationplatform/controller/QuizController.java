package com.example.onlineeducationplatform.controller;

import java.util.List;

import com.example.onlineeducationplatform.config.JwtUtil;
import com.example.onlineeducationplatform.model.Quiz;
import com.example.onlineeducationplatform.model.User;
import com.example.onlineeducationplatform.service.QuizService;
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
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Get all quizzes
    @GetMapping
    public ResponseEntity<?> getAllQuizzes() {
        try {
            List<Quiz> quizzes = quizService.getAllQuizzes();
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch quizzes: " + e.getMessage());
        }
    }

    // Get quizzes by course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getQuizzesByCourse(@PathVariable Integer courseId) {
        try {
            List<Quiz> quizzes = quizService.getQuizzesByCourse(courseId);
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch quizzes: " + e.getMessage());
        }
    }

    // Get published quizzes by course
    @GetMapping("/course/{courseId}/published")
    public ResponseEntity<?> getPublishedQuizzesByCourse(@PathVariable Integer courseId) {
        try {
            List<Quiz> quizzes = quizService.getPublishedQuizzesByCourse(courseId);
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch quizzes: " + e.getMessage());
        }
    }

    // Get quizzes by lesson
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<?> getQuizzesByLesson(@PathVariable Integer lessonId) {
        try {
            List<Quiz> quizzes = quizService.getQuizzesByLesson(lessonId);
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch quizzes: " + e.getMessage());
        }
    }

    // Get published quizzes by lesson
    @GetMapping("/lesson/{lessonId}/published")
    public ResponseEntity<?> getPublishedQuizzesByLesson(@PathVariable Integer lessonId) {
        try {
            List<Quiz> quizzes = quizService.getPublishedQuizzesByLesson(lessonId);
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch quizzes: " + e.getMessage());
        }
    }

    // Get single quiz
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable Integer id) {
        try {
            Quiz quiz = quizService.getQuizById(id);
            if (quiz != null) {
                return ResponseEntity.ok(quiz);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch quiz: " + e.getMessage());
        }
    }

    // Create quiz (instructor/admin only)
    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody Quiz quiz,
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

            quiz.setCreatedBy(user.getId());
            quizService.addQuiz(quiz);
            return new ResponseEntity<>(quiz, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Update quiz (instructor/admin only)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuiz(@PathVariable Integer id,
                                        @RequestBody Quiz quiz,
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
                return new ResponseEntity<>("Only admin can update quizzes", HttpStatus.FORBIDDEN);
            }

            quiz.setId(id);
            quizService.updateQuiz(quiz);
            return ResponseEntity.ok("Quiz updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Publish quiz (instructor/admin only)
    @PutMapping("/{id}/publish")
    public ResponseEntity<?> publishQuiz(@PathVariable Integer id,
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
                return new ResponseEntity<>("Only admin can publish quizzes", HttpStatus.FORBIDDEN);
            }

            quizService.updateQuizStatus(id, "PUBLISHED");
            return ResponseEntity.ok("Quiz published");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Delete quiz (admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Integer id,
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
                return new ResponseEntity<>("Only admin can delete quizzes", HttpStatus.FORBIDDEN);
            }

            quizService.deleteQuiz(id);
            return ResponseEntity.ok("Quiz deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Get quiz count
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<?> getQuizCount(@PathVariable Integer courseId) {
        try {
            Integer count = quizService.getQuizCountByCourse(courseId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
