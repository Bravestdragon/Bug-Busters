package com.example.onlineeducationplatform.service.impl;

import java.util.List;

import com.example.onlineeducationplatform.mapper.LessonMapper;
import com.example.onlineeducationplatform.model.Lesson;
import com.example.onlineeducationplatform.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonMapper lessonMapper;

    @Override
    public void addLesson(Lesson lesson) {
        lessonMapper.insertLesson(lesson);
    }

    @Override
    public Lesson getLessonById(Integer id) {
        return lessonMapper.selectLessonById(id);
    }

    @Override
    public List<Lesson> getLessonsByCourse(Integer courseId) {
        return lessonMapper.selectLessonsByCourse(courseId);
    }

    @Override
    public List<Lesson> getLessonsByCourseOrdered(Integer courseId) {
        return lessonMapper.selectLessonsByCourseOrdered(courseId);
    }

    @Override
    public List<Lesson> getPublishedLessonsByCourse(Integer courseId) {
        return lessonMapper.selectPublishedLessonsByCourse(courseId);
    }

    @Override
    public Lesson getLessonByIdAndCourse(Integer id, Integer courseId) {
        return lessonMapper.selectLessonByIdAndCourse(id, courseId);
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonMapper.selectAllLessons();
    }

    @Override
    public void updateLesson(Lesson lesson) {
        lessonMapper.updateLesson(lesson);
    }

    @Override
    public void updateLessonStatus(Integer id, String status) {
        lessonMapper.updateLessonStatus(id, status);
    }

    @Override
    public void deleteLesson(Integer id) {
        lessonMapper.deleteLesson(id);
    }

    @Override
    public void deleteLessonsByCourse(Integer courseId) {
        lessonMapper.deleteLessonsByCourse(courseId);
    }

    @Override
    public Integer getLessonCountByCourse(Integer courseId) {
        return lessonMapper.countLessonsByCourse(courseId);
    }
}
