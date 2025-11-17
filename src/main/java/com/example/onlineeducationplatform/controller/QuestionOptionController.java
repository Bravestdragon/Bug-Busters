package com.example.onlineeducationplatform.controller;

import java.util.List;

import com.example.onlineeducationplatform.config.JwtUtil;
import com.example.onlineeducationplatform.model.QuestionOption;
import com.example.onlineeducationplatform.service.QuestionOptionService;
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
@RequestMapping("/api/question-options")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QuestionOptionController {

    @Autowired
    private QuestionOptionService questionOptionService;

    @Autowired
    private JwtUtil jwtUtil;

    // Get options by question
    @GetMapping("/question/{questionId}")
    public ResponseEntity<?> getOptionsByQuestion(@PathVariable Integer questionId) {
        try {
            List<QuestionOption> options = questionOptionService.getOptionsByQuestion(questionId);
            return ResponseEntity.ok(options);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch options: " + e.getMessage());
        }
    }

    // Get single option
    @GetMapping("/{id}")
    public ResponseEntity<?> getOptionById(@PathVariable Integer id) {
        try {
            QuestionOption option = questionOptionService.getQuestionOptionById(id);
            if (option != null) {
                return ResponseEntity.ok(option);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch option: " + e.getMessage());
        }
    }

    // Create option (admin only)
    @PostMapping
    public ResponseEntity<?> createOption(@RequestBody QuestionOption option,
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
                return new ResponseEntity<>("Only admin can create options", HttpStatus.FORBIDDEN);
            }

            questionOptionService.addQuestionOption(option);
            return new ResponseEntity<>(option, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Update option (admin only)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOption(@PathVariable Integer id,
                                          @RequestBody QuestionOption option,
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
                return new ResponseEntity<>("Only admin can update options", HttpStatus.FORBIDDEN);
            }

            option.setId(id);
            questionOptionService.updateQuestionOption(option);
            return ResponseEntity.ok("Option updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Delete option (admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOption(@PathVariable Integer id,
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
                return new ResponseEntity<>("Only admin can delete options", HttpStatus.FORBIDDEN);
            }

            questionOptionService.deleteQuestionOption(id);
            return ResponseEntity.ok("Option deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
