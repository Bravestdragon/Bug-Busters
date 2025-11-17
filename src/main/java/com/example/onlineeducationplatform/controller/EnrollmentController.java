package com.example.onlineeducationplatform.controller;

import java.util.List;

import com.example.onlineeducationplatform.config.JwtUtil;
import com.example.onlineeducationplatform.model.Enrollment;
import com.example.onlineeducationplatform.model.User;
import com.example.onlineeducationplatform.service.EnrollmentService;
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
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Enroll student in course (authenticated users)
    @PostMapping
    public ResponseEntity<?> enrollStudent(@RequestBody Enrollment enrollment,
                                           @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Verify authentication
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>("Unauthorized: login required", HttpStatus.UNAUTHORIZED);
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

            // Set student info from authenticated user
            enrollment.setStudentId(user.getId());
            enrollment.setStudentName(user.getUsername());
            enrollment.setEnrollmentStatus("ACTIVE");

            // Check if already enrolled
            Enrollment existing = enrollmentService.checkEnrollment(enrollment.getCourseId(), user.getId());
            if (existing != null) {
                return new ResponseEntity<>("Already enrolled in this course", HttpStatus.BAD_REQUEST);
            }

            enrollmentService.enrollStudent(enrollment);
            return new ResponseEntity<>(enrollment, HttpStatus.CREATED);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Get enrollment by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable Integer id) {
        try {
            Enrollment enrollment = enrollmentService.getEnrollmentById(id);
            if (enrollment != null) {
                return ResponseEntity.ok(enrollment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch enrollment: " + e.getMessage());
        }
    }

    // Get enrollments by course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getEnrollmentsByCourse(@PathVariable Integer courseId) {
        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch enrollments: " + e.getMessage());
        }
    }

    // Get enrollments by student (authenticated users - own enrollments only)
    @GetMapping("/student/my-courses")
    public ResponseEntity<?> getMyEnrollments(@RequestHeader(value = "Authorization", required = false) String authHeader) {
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

            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(user.getId());
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Get all enrollments (admin only)
    @GetMapping
    public ResponseEntity<?> getAllEnrollments() {
        try {
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch enrollments: " + e.getMessage());
        }
    }

    // Update enrollment status (admin or course instructor)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateEnrollmentStatus(@PathVariable Integer id,
                                                     @RequestParam String status,
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
            
            // Only admin can update status
            if (!"axzil".equalsIgnoreCase(requester)) {
                return new ResponseEntity<>("Only admin can update enrollment status", HttpStatus.FORBIDDEN);
            }

            enrollmentService.updateEnrollmentStatus(id, status);
            return ResponseEntity.ok("Enrollment status updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Update progress percentage
    @PutMapping("/{id}/progress")
    public ResponseEntity<?> updateProgressPercentage(@PathVariable Integer id,
                                                       @RequestParam Double progress,
                                                       @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }

            enrollmentService.updateProgressPercentage(id, progress);
            return ResponseEntity.ok("Progress updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Drop course (authenticated users - own enrollments only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> dropCourse(@PathVariable Integer id,
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

            // Verify ownership
            Enrollment enrollment = enrollmentService.getEnrollmentById(id);
            if (enrollment == null) {
                return new ResponseEntity<>("Enrollment not found", HttpStatus.NOT_FOUND);
            }

            if (!enrollment.getStudentId().equals(user.getId()) && !"axzil".equalsIgnoreCase(requester)) {
                return new ResponseEntity<>("You can only drop your own enrollments", HttpStatus.FORBIDDEN);
            }

            enrollmentService.dropEnrollment(id);
            return ResponseEntity.ok("Course dropped successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Get enrollment count for course
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<?> getEnrollmentCount(@PathVariable Integer courseId) {
        try {
            Integer count = enrollmentService.getEnrollmentCountForCourse(courseId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
