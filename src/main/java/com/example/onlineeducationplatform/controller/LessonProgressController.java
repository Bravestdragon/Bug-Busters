package com.example.onlineeducationplatform.controller;

import java.util.List;

import com.example.onlineeducationplatform.config.JwtUtil;
import com.example.onlineeducationplatform.model.LessonProgress;
import com.example.onlineeducationplatform.model.User;
import com.example.onlineeducationplatform.service.LessonProgressService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lesson-progress")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LessonProgressController {

    @Autowired
    private LessonProgressService progressService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Get student's progress for a course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getCourseProgress(@PathVariable Integer courseId,
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

            List<LessonProgress> progress = progressService.getProgressByCourse(courseId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Get student's progress for enrollment
    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<?> getEnrollmentProgress(@PathVariable Integer enrollmentId,
                                                   @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }

            List<LessonProgress> progress = progressService.getProgressByEnrollment(enrollmentId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Get progress for specific lesson
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<?> getLessonProgress(@PathVariable Integer lessonId) {
        try {
            List<LessonProgress> progress = progressService.getProgressByLesson(lessonId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Create or initialize progress
    @PostMapping
    public ResponseEntity<?> createProgress(@RequestBody LessonProgress progress,
                                            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }

            progressService.createProgress(progress);
            return new ResponseEntity<>(progress, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Mark lesson as complete
    @PutMapping("/lesson/{lessonId}/complete")
    public ResponseEntity<?> markLessonComplete(@PathVariable Integer lessonId,
                                                @RequestParam Integer enrollmentId,
                                                @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }

            progressService.markLessonComplete(enrollmentId, lessonId);
            return ResponseEntity.ok("Lesson marked as complete");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Update time spent on lesson
    @PutMapping("/lesson/{lessonId}/time")
    public ResponseEntity<?> updateTimeSpent(@PathVariable Integer lessonId,
                                             @RequestParam Integer enrollmentId,
                                             @RequestParam Integer minutes,
                                             @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }

            progressService.updateTimeSpent(enrollmentId, lessonId, minutes);
            return ResponseEntity.ok("Time updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Get completed lesson count
    @GetMapping("/completed/count")
    public ResponseEntity<?> getCompletedCount(@RequestParam Integer enrollmentId) {
        try {
            Integer count = progressService.getCompletedLessonCount(enrollmentId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Delete progress
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProgress(@PathVariable Integer id,
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
                return new ResponseEntity<>("Only admin can delete progress", HttpStatus.FORBIDDEN);
            }

            progressService.deleteProgress(id);
            return ResponseEntity.ok("Progress deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
