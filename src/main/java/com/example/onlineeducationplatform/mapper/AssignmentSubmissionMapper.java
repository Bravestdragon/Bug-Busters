package com.example.onlineeducationplatform.mapper;

import com.example.onlineeducationplatform.model.AssignmentSubmission;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AssignmentSubmissionMapper {
    int insert(AssignmentSubmission submission);
    AssignmentSubmission selectById(Long id);
    List<AssignmentSubmission> selectByAssignmentId(Long assignmentId);
    List<AssignmentSubmission> selectByEnrollmentId(Long enrollmentId);
    List<AssignmentSubmission> selectByUserId(Long userId);
    List<AssignmentSubmission> selectByStatus(String status);
    AssignmentSubmission selectByAssignmentAndUser(Long assignmentId, Long userId);
    int update(AssignmentSubmission submission);
    int updateStatus(Long id, String status);
    int gradeSubmission(Long id, Integer score, String feedback);
    int delete(Long id);
    int countByAssignmentId(Long assignmentId);
    int countByEnrollmentId(Long enrollmentId);
}
