package com.example.onlineeducationplatform.service;

import com.example.onlineeducationplatform.model.Course;
import java.util.List;

public interface CourseService {
    void addCourse(Course course);

    Course getCourseById(Integer id);

    List<Course> getAllCourses();

    List<Course> getCoursesByCategory(String category);

    List<Course> getCoursesByInstructor(Integer instructorId);

    List<Course> searchCourses(String keyword);

    void updateCourse(Course course);

    void deleteCourse(Integer id);

    List<String> getAllCategories();
}
