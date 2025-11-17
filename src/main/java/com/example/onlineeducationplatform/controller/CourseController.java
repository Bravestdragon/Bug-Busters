package com.example.onlineeducationplatform.controller;

import java.util.List;

import com.example.onlineeducationplatform.config.JwtUtil;
import com.example.onlineeducationplatform.model.Course;
import com.example.onlineeducationplatform.model.User;
import com.example.onlineeducationplatform.service.CourseService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Get all published courses
    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch courses: " + e.getMessage());
        }
    }

    // Get course by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Integer id) {
        try {
            Course course = courseService.getCourseById(id);
            if (course != null) {
                return ResponseEntity.ok(course);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch course: " + e.getMessage());
        }
    }

    // Get courses by category
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getCoursesByCategory(@PathVariable String category) {
        try {
            List<Course> courses = courseService.getCoursesByCategory(category);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch courses: " + e.getMessage());
        }
    }

    // Search courses by keyword
    @GetMapping("/search")
    public ResponseEntity<?> searchCourses(@RequestParam String keyword) {
        try {
            List<Course> courses = courseService.searchCourses(keyword);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search courses: " + e.getMessage());
        }
    }

    // Get all categories
    @GetMapping("/meta/categories")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<String> categories = courseService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch categories: " + e.getMessage());
        }
    }

    // Create new course (authenticated users)
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course,
                                          @org.springframework.web.bind.annotation.RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Check if user is authenticated (has Authorization header)
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

            // Set course details
            course.setInstructorId(user.getId());
            course.setInstructorName(user.getUsername());
            course.setStatus("DRAFT"); // Always start as draft

            courseService.addCourse(course);
            return new ResponseEntity<>(course, HttpStatus.CREATED);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Update course (only instructor or admin)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Integer id,
                                          @RequestBody Course course,
                                          @org.springframework.web.bind.annotation.RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Extract requester from JWT
            String requester = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    requester = jwtUtil.extractUsername(token);
                }
            }

            // Check if course exists
            Course existingCourse = courseService.getCourseById(id);
            if (existingCourse == null) {
                return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
            }

            // Only course instructor or admin 'axzil' can update
            boolean isInstructor = requester != null && requester.equalsIgnoreCase(existingCourse.getInstructorName());
            boolean isAdmin = requester != null && "axzil".equalsIgnoreCase(requester);

            if (!isInstructor && !isAdmin) {
                return new ResponseEntity<>("Forbidden: only the instructor or admin can update this course", HttpStatus.FORBIDDEN);
            }

            course.setId(id);
            course.setInstructorId(existingCourse.getInstructorId());
            course.setInstructorName(existingCourse.getInstructorName());
            courseService.updateCourse(course);
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update course: " + e.getMessage());
        }
    }

    // Delete course (only instructor or admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Integer id,
                                          @org.springframework.web.bind.annotation.RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Extract requester from JWT
            String requester = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    requester = jwtUtil.extractUsername(token);
                }
            }

            // Check if course exists
            Course course = courseService.getCourseById(id);
            if (course == null) {
                return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
            }

            // Only course instructor or admin 'axzil' can delete
            boolean isInstructor = requester != null && requester.equalsIgnoreCase(course.getInstructorName());
            boolean isAdmin = requester != null && "axzil".equalsIgnoreCase(requester);

            if (!isInstructor && !isAdmin) {
                return new ResponseEntity<>("Forbidden: only the instructor or admin can delete this course", HttpStatus.FORBIDDEN);
            }

            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete course: " + e.getMessage());
        }
    }

    // Get courses created by current user
    @GetMapping("/my-courses")
    public ResponseEntity<?> getMyCourses(@org.springframework.web.bind.annotation.RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Extract requester from JWT
            String requester = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    requester = jwtUtil.extractUsername(token);
                }
            }

            if (requester == null) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            // Get user to find their ID
            User user = userService.getUserByUsername(requester);
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            List<Course> courses = courseService.getCoursesByInstructor(user.getId());
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch courses: " + e.getMessage());
        }
    }
}
