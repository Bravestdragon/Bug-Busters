package com.example.onlineeducationplatform.service.impl;

import com.example.onlineeducationplatform.mapper.AssignmentMapper;
import com.example.onlineeducationplatform.model.Assignment;
import com.example.onlineeducationplatform.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    
    @Autowired
    private AssignmentMapper assignmentMapper;

    @Override
    public Assignment createAssignment(Assignment assignment) {
        assignmentMapper.insert(assignment);
        return assignment;
    }

    @Override
    public Assignment getAssignmentById(Long id) {
        return assignmentMapper.selectById(id);
    }

    @Override
    public List<Assignment> getActiveAssignments() {
        return assignmentMapper.selectByStatus("ACTIVE");
    }

    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentMapper.selectAll();
    }

    @Override
    public List<Assignment> getAssignmentsByCourse(Long courseId) {
        return assignmentMapper.selectByCourseId(courseId);
    }

    @Override
    public List<Assignment> getAssignmentsByLesson(Long lessonId) {
        return assignmentMapper.selectByLessonId(lessonId);
    }

    @Override
    public Assignment updateAssignment(Assignment assignment) {
        assignmentMapper.update(assignment);
        return assignment;
    }

    @Override
    public boolean deleteAssignment(Long id) {
        return assignmentMapper.delete(id) > 0;
    }

    @Override
    public int countAssignmentsByCourse(Long courseId) {
        return assignmentMapper.countByCourseId(courseId);
    }
}
