package com.example.onlineeducationplatform.model;

import java.time.LocalDateTime;

public class AssignmentSubmission {
    private Long id;
    private Long assignmentId;
    private Long enrollmentId;
    private Long userId;
    private String submissionFile; // File path or URL
    private String submissionText; // Text submission
    private LocalDateTime submittedAt;
    private String status; // PENDING, SUBMITTED, GRADED
    private Integer score;
    private String feedback;
    private LocalDateTime gradedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AssignmentSubmission() {}

    public AssignmentSubmission(Long assignmentId, Long enrollmentId, Long userId, 
                                String submissionFile, String submissionText) {
        this.assignmentId = assignmentId;
        this.enrollmentId = enrollmentId;
        this.userId = userId;
        this.submissionFile = submissionFile;
        this.submissionText = submissionText;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }

    public Long getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getSubmissionFile() { return submissionFile; }
    public void setSubmissionFile(String submissionFile) { this.submissionFile = submissionFile; }

    public String getSubmissionText() { return submissionText; }
    public void setSubmissionText(String submissionText) { this.submissionText = submissionText; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public LocalDateTime getGradedAt() { return gradedAt; }
    public void setGradedAt(LocalDateTime gradedAt) { this.gradedAt = gradedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
