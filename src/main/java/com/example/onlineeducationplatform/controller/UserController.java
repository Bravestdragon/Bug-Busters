package com.example.onlineeducationplatform.controller;

import java.util.List;

import com.example.onlineeducationplatform.config.JwtUtil;
import com.example.onlineeducationplatform.config.AuthResponse;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineeducationplatform.model.User;
import com.example.onlineeducationplatform.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        try {
            User user = userService.getUserByUsername(loginUser.getUsername());
            if (user != null && userService.checkPassword(loginUser.getPassword(), user.getPassword())) {
                // Generate JWT token
                String token = jwtUtil.generateToken(user.getUsername());
                // Return user info including role
                AuthResponse response = new AuthResponse(token, "Login successful", user.getUsername());
                response.setRole(user.getRole());
                return ResponseEntity.ok().body(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed: " + e.getMessage());
        }
    }

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            if (userService.getUserByUsername(user.getUsername()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch users: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch user: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user,
                                     @org.springframework.web.bind.annotation.RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Only the admin "axzil" may set role on creation. Otherwise default to USER.
            String creator = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    creator = jwtUtil.extractUsername(token);
                }
            }

            if (user.getRole() != null && !"axzil".equalsIgnoreCase(creator)) {
                user.setRole("USER");
            }

            int result = userService.addUser(user);
            if (result > 0) {
                return new ResponseEntity<>(user, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Failed to create user", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,
                                        @RequestBody User user,
                                        @org.springframework.web.bind.annotation.RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Check if user being updated exists
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            // Determine requester username from JWT (if present)
            String requester = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    requester = jwtUtil.extractUsername(token);
                }
            }

            // If role is being changed, only allow if requester is admin "axzil"
            if (user.getRole() != null && !user.getRole().equalsIgnoreCase(existingUser.getRole())) {
                if (!"axzil".equalsIgnoreCase(requester)) {
                    return new ResponseEntity<>("Forbidden: only admin can change roles", HttpStatus.FORBIDDEN);
                }
            }

            // Only allow edits if requester is the account owner or the admin 'axzil'
            if (requester == null || (!requester.equalsIgnoreCase(existingUser.getUsername()) && !"axzil".equalsIgnoreCase(requester))) {
                return new ResponseEntity<>("Forbidden: you may only edit your own profile", HttpStatus.FORBIDDEN);
            }

            user.setId(id);
            int result = userService.updateUser(user);
            if (result > 0) {
                return ResponseEntity.ok(user);
            } else {
                return new ResponseEntity<>("Failed to update user", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id,
                                        @org.springframework.web.bind.annotation.RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            // Determine requester username from JWT (if present)
            String requester = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    requester = jwtUtil.extractUsername(token);
                }
            }

            // Only admin "axzil" can delete users
            if (!"axzil".equalsIgnoreCase(requester)) {
                return new ResponseEntity<>("Forbidden: only admin can delete users", HttpStatus.FORBIDDEN);
            }

            int result = userService.deleteUser(id);
            if (result > 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("Failed to delete user", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user: " + e.getMessage());
        }
    }
}