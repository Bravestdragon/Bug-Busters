package com.example.onlineeducationplatform.controller;

import com.example.onlineeducationplatform.model.Assignment;
import com.example.onlineeducationplatform.service.AssignmentService;
import com.example.onlineeducationplatform.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "*")
public class AssignmentController {
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private JwtUtil jwtUtil;

    // Get all assignments
    @GetMapping
    public ResponseEntity<?> getAllAssignments(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        List<Assignment> assignments = assignmentService.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }

    // Create assignment (Admin only)
    @PostMapping
    public ResponseEntity<?> createAssignment(@RequestBody Assignment assignment, 
                                            @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        String username = jwtUtil.extractUsername(token);
        if (!username.equals("axzil")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required");
        }
        
        Assignment created = assignmentService.createAssignment(assignment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Get assignment by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable Long id, 
                                              @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        Assignment assignment = assignmentService.getAssignmentById(id);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment not found");
        }
        return ResponseEntity.ok(assignment);
    }

    // Get assignments by course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getAssignmentsByCourse(@PathVariable Long courseId, 
                                                   @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(courseId);
        return ResponseEntity.ok(assignments);
    }

    // Get assignments by lesson
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<?> getAssignmentsByLesson(@PathVariable Long lessonId, 
                                                   @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        List<Assignment> assignments = assignmentService.getAssignmentsByLesson(lessonId);
        return ResponseEntity.ok(assignments);
    }

    // Get active assignments
    @GetMapping("/active")
    public ResponseEntity<?> getActiveAssignments(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        List<Assignment> assignments = assignmentService.getActiveAssignments();
        return ResponseEntity.ok(assignments);
    }

    // Update assignment (Admin only)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable Long id, @RequestBody Assignment assignment, 
                                            @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        String username = jwtUtil.extractUsername(token);
        if (!username.equals("axzil")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required");
        }
        
        assignment.setId(id);
        Assignment updated = assignmentService.updateAssignment(assignment);
        return ResponseEntity.ok(updated);
    }

    // Delete assignment (Admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long id, 
                                            @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        String username = jwtUtil.extractUsername(token);
        if (!username.equals("axzil")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required");
        }
        
        boolean deleted = assignmentService.deleteAssignment(id);
        if (deleted) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Assignment deleted successfully");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment not found");
    }

    // Count assignments by course
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<?> countAssignmentsByCourse(@PathVariable Long courseId) {
        int count = assignmentService.countAssignmentsByCourse(courseId);
        Map<String, Integer> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}
