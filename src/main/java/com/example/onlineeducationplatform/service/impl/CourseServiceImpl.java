package com.example.onlineeducationplatform.service.impl;

import com.example.onlineeducationplatform.mapper.CourseMapper;
import com.example.onlineeducationplatform.model.Course;
import com.example.onlineeducationplatform.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public void addCourse(Course course) {
        courseMapper.insertCourse(course);
    }

    @Override
    public Course getCourseById(Integer id) {
        return courseMapper.selectCourseById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseMapper.selectAllCourses();
    }

    @Override
    public List<Course> getCoursesByCategory(String category) {
        return courseMapper.selectCoursesByCategory(category);
    }

    @Override
    public List<Course> getCoursesByInstructor(Integer instructorId) {
        return courseMapper.selectCoursesByInstructor(instructorId);
    }

    @Override
    public List<Course> searchCourses(String keyword) {
        return courseMapper.searchCourses(keyword);
    }

    @Override
    public void updateCourse(Course course) {
        courseMapper.updateCourse(course);
    }

    @Override
    public void deleteCourse(Integer id) {
        courseMapper.deleteCourse(id);
    }

    @Override
    public List<String> getAllCategories() {
        return courseMapper.selectAllCategories();
    }
}
