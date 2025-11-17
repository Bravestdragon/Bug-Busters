package com.example.onlineeducationplatform.controller;

import java.util.List;

import com.example.onlineeducationplatform.config.JwtUtil;
import com.example.onlineeducationplatform.model.Lesson;
import com.example.onlineeducationplatform.model.User;
import com.example.onlineeducationplatform.service.LessonService;
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
@RequestMapping("/api/lessons")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Get all lessons for a course (public - for enrolled students)
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getLessonsByCourse(@PathVariable Integer courseId) {
        try {
            List<Lesson> lessons = lessonService.getLessonsByCourseOrdered(courseId);
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch lessons: " + e.getMessage());
        }
    }

    // Get published lessons only (for non-instructors)
    @GetMapping("/course/{courseId}/published")
    public ResponseEntity<?> getPublishedLessonsByCourse(@PathVariable Integer courseId) {
        try {
            List<Lesson> lessons = lessonService.getPublishedLessonsByCourse(courseId);
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch lessons: " + e.getMessage());
        }
    }

    // Get single lesson
    @GetMapping("/{id}")
    public ResponseEntity<?> getLessonById(@PathVariable Integer id) {
        try {
            Lesson lesson = lessonService.getLessonById(id);
            if (lesson != null) {
                return ResponseEntity.ok(lesson);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch lesson: " + e.getMessage());
        }
    }

    // Create lesson (instructor/admin only)
    @PostMapping
    public ResponseEntity<?> createLesson(@RequestBody Lesson lesson,
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

            lessonService.addLesson(lesson);
            return new ResponseEntity<>(lesson, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Update lesson (instructor/admin only)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLesson(@PathVariable Integer id,
                                          @RequestBody Lesson lesson,
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
            
            // Only admin can update lessons
            if (!"axzil".equalsIgnoreCase(requester)) {
                return new ResponseEntity<>("Only admin can update lessons", HttpStatus.FORBIDDEN);
            }

            lesson.setId(id);
            lessonService.updateLesson(lesson);
            return ResponseEntity.ok("Lesson updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Publish lesson (instructor/admin only)
    @PutMapping("/{id}/publish")
    public ResponseEntity<?> publishLesson(@PathVariable Integer id,
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
                return new ResponseEntity<>("Only admin can publish lessons", HttpStatus.FORBIDDEN);
            }

            lessonService.updateLessonStatus(id, "PUBLISHED");
            return ResponseEntity.ok("Lesson published");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Delete lesson (admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Integer id,
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
                return new ResponseEntity<>("Only admin can delete lessons", HttpStatus.FORBIDDEN);
            }

            lessonService.deleteLesson(id);
            return ResponseEntity.ok("Lesson deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Get lesson count for course
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<?> getLessonCount(@PathVariable Integer courseId) {
        try {
            Integer count = lessonService.getLessonCountByCourse(courseId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
