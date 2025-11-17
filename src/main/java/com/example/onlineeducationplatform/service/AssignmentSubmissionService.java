package com.example.onlineeducationplatform.service;

import com.example.onlineeducationplatform.model.AssignmentSubmission;
import java.util.List;

public interface AssignmentSubmissionService {
    AssignmentSubmission submitAssignment(AssignmentSubmission submission);
    AssignmentSubmission getSubmissionById(Long id);
    List<AssignmentSubmission> getSubmissionsByAssignment(Long assignmentId);
    List<AssignmentSubmission> getSubmissionsByStudent(Long userId);
    AssignmentSubmission getStudentSubmission(Long assignmentId, Long userId);
    AssignmentSubmission gradeSubmission(Long submissionId, Integer score, String feedback);
    AssignmentSubmission updateSubmission(AssignmentSubmission submission);
    boolean deleteSubmission(Long id);
    List<AssignmentSubmission> getPendingGrades();
}
