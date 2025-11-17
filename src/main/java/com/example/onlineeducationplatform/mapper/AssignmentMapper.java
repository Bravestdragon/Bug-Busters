package com.example.onlineeducationplatform.mapper;

import com.example.onlineeducationplatform.model.Assignment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AssignmentMapper {
    // Create
    int insert(Assignment assignment);

    // Read
    Assignment selectById(Long id);
    List<Assignment> selectByCourseId(Long courseId);
    List<Assignment> selectByLessonId(Long lessonId);
    List<Assignment> selectByStatus(String status);
    List<Assignment> selectAll();

    // Update
    int update(Assignment assignment);
    int updateStatus(Long id, String status);

    // Delete
    int delete(Long id);

    // Count
    int countByCourseId(Long courseId);
    int countByLessonId(Long lessonId);
}
