package com.example.onlineeducationplatform.mapper;

import com.example.onlineeducationplatform.model.Course;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CourseMapper {
    void insertCourse(Course course);

    Course selectCourseById(Integer id);

    List<Course> selectAllCourses();

    List<Course> selectCoursesByCategory(String category);

    List<Course> selectCoursesByInstructor(Integer instructorId);

    List<Course> searchCourses(String keyword);

    void updateCourse(Course course);

    void deleteCourse(Integer id);

    List<String> selectAllCategories();
}
