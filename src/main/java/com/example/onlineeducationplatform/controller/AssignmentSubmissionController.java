package com.example.onlineeducationplatform.controller;

import com.example.onlineeducationplatform.model.AssignmentSubmission;
import com.example.onlineeducationplatform.service.AssignmentSubmissionService;
import com.example.onlineeducationplatform.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/assignment-submissions")
@CrossOrigin(origins = "*")
public class AssignmentSubmissionController {
    
    @Autowired
    private AssignmentSubmissionService submissionService;
    
    @Autowired
    private JwtUtil jwtUtil;

    // Submit assignment (Student)
    @PostMapping
    public ResponseEntity<?> submitAssignment(@RequestBody AssignmentSubmission submission, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        AssignmentSubmission submitted = submissionService.submitAssignment(submission);
        return ResponseEntity.status(HttpStatus.CREATED).body(submitted);
    }

    // Get submission by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSubmissionById(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        AssignmentSubmission submission = submissionService.getSubmissionById(id);
        if (submission == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Submission not found");
        }
        return ResponseEntity.ok(submission);
    }

    // Get submissions by assignment
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<?> getSubmissionsByAssignment(@PathVariable Long assignmentId, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        List<AssignmentSubmission> submissions = submissionService.getSubmissionsByAssignment(assignmentId);
        return ResponseEntity.ok(submissions);
    }

    // Get submissions by student
    @GetMapping("/student/{userId}")
    public ResponseEntity<?> getSubmissionsByStudent(@PathVariable Long userId, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        List<AssignmentSubmission> submissions = submissionService.getSubmissionsByStudent(userId);
        return ResponseEntity.ok(submissions);
    }

    // Get student's submission for specific assignment
    @GetMapping("/assignment/{assignmentId}/student/{userId}")
    public ResponseEntity<?> getStudentSubmission(@PathVariable Long assignmentId, @PathVariable Long userId, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        AssignmentSubmission submission = submissionService.getStudentSubmission(assignmentId, userId);
        if (submission == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Submission not found");
        }
        return ResponseEntity.ok(submission);
    }

    // Grade submission (Admin/Teacher only)
    @PutMapping("/{id}/grade")
    public ResponseEntity<?> gradeSubmission(@PathVariable Long id, @RequestBody Map<String, Object> gradeData, @RequestHeader(value = "Authorization", required = false) String token) {
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
        
        Integer score = ((Number) gradeData.get("score")).intValue();
        String feedback = (String) gradeData.get("feedback");
        AssignmentSubmission graded = submissionService.gradeSubmission(id, score, feedback);
        return ResponseEntity.ok(graded);
    }

    // Update submission
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubmission(@PathVariable Long id, @RequestBody AssignmentSubmission submission, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        submission.setId(id);
        AssignmentSubmission updated = submissionService.updateSubmission(submission);
        return ResponseEntity.ok(updated);
    }

    // Delete submission
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubmission(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String token) {
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
        
        boolean deleted = submissionService.deleteSubmission(id);
        if (deleted) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Submission deleted successfully");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Submission not found");
    }

    // Get pending grades (Admin/Teacher only)
    @GetMapping("/pending-grades")
    public ResponseEntity<?> getPendingGrades(@RequestHeader(value = "Authorization", required = false) String token) {
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
        
        List<AssignmentSubmission> pending = submissionService.getPendingGrades();
        return ResponseEntity.ok(pending);
    }
}
