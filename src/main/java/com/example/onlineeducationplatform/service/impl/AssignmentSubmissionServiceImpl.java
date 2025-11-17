package com.example.onlineeducationplatform.service.impl;

import com.example.onlineeducationplatform.mapper.AssignmentSubmissionMapper;
import com.example.onlineeducationplatform.model.AssignmentSubmission;
import com.example.onlineeducationplatform.service.AssignmentSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentSubmissionServiceImpl implements AssignmentSubmissionService {
    
    @Autowired
    private AssignmentSubmissionMapper submissionMapper;

    @Override
    public AssignmentSubmission submitAssignment(AssignmentSubmission submission) {
        submission.setStatus("SUBMITTED");
        submission.setSubmittedAt(LocalDateTime.now());
        submissionMapper.insert(submission);
        return submission;
    }

    @Override
    public AssignmentSubmission getSubmissionById(Long id) {
        return submissionMapper.selectById(id);
    }

    @Override
    public List<AssignmentSubmission> getSubmissionsByAssignment(Long assignmentId) {
        return submissionMapper.selectByAssignmentId(assignmentId);
    }

    @Override
    public List<AssignmentSubmission> getSubmissionsByStudent(Long userId) {
        return submissionMapper.selectByUserId(userId);
    }

    @Override
    public AssignmentSubmission getStudentSubmission(Long assignmentId, Long userId) {
        return submissionMapper.selectByAssignmentAndUser(assignmentId, userId);
    }

    @Override
    public AssignmentSubmission gradeSubmission(Long submissionId, Integer score, String feedback) {
        submissionMapper.gradeSubmission(submissionId, score, feedback);
        return submissionMapper.selectById(submissionId);
    }

    @Override
    public AssignmentSubmission updateSubmission(AssignmentSubmission submission) {
        submissionMapper.update(submission);
        return submission;
    }

    @Override
    public boolean deleteSubmission(Long id) {
        return submissionMapper.delete(id) > 0;
    }

    @Override
    public List<AssignmentSubmission> getPendingGrades() {
        return submissionMapper.selectByStatus("SUBMITTED");
    }
}
