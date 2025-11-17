package com.example.onlineeducationplatform.service;

import com.example.onlineeducationplatform.model.Assignment;
import java.util.List;

public interface AssignmentService {
    Assignment createAssignment(Assignment assignment);
    Assignment getAssignmentById(Long id);
    List<Assignment> getActiveAssignments();
    List<Assignment> getAllAssignments();
    List<Assignment> getAssignmentsByCourse(Long courseId);
    List<Assignment> getAssignmentsByLesson(Long lessonId);
    Assignment updateAssignment(Assignment assignment);
    boolean deleteAssignment(Long id);
    int countAssignmentsByCourse(Long courseId);
}
