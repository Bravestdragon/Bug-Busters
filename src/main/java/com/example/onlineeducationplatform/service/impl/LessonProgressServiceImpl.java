package com.example.onlineeducationplatform.service.impl;

import java.util.List;

import com.example.onlineeducationplatform.mapper.LessonProgressMapper;
import com.example.onlineeducationplatform.model.LessonProgress;
import com.example.onlineeducationplatform.service.LessonProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonProgressServiceImpl implements LessonProgressService {

    @Autowired
    private LessonProgressMapper progressMapper;

    @Override
    public void createProgress(LessonProgress progress) {
        progressMapper.insertProgress(progress);
    }

    @Override
    public LessonProgress getProgressById(Integer id) {
        return progressMapper.selectProgressById(id);
    }

    @Override
    public LessonProgress getProgress(Integer enrollmentId, Integer lessonId) {
        return progressMapper.selectProgressByEnrollmentAndLesson(enrollmentId, lessonId);
    }

    @Override
    public List<LessonProgress> getProgressByEnrollment(Integer enrollmentId) {
        return progressMapper.selectProgressByEnrollment(enrollmentId);
    }

    @Override
    public List<LessonProgress> getProgressByLesson(Integer lessonId) {
        return progressMapper.selectProgressByLesson(lessonId);
    }

    @Override
    public List<LessonProgress> getProgressByStudent(Integer studentId) {
        return progressMapper.selectProgressByStudent(studentId);
    }

    @Override
    public List<LessonProgress> getProgressByCourse(Integer courseId) {
        return progressMapper.selectProgressByCourse(courseId);
    }

    @Override
    public void updateProgress(LessonProgress progress) {
        progressMapper.updateProgress(progress);
    }

    @Override
    public void markLessonComplete(Integer enrollmentId, Integer lessonId) {
        progressMapper.markLessonComplete(enrollmentId, lessonId);
    }

    @Override
    public void updateTimeSpent(Integer enrollmentId, Integer lessonId, Integer minutes) {
        progressMapper.updateTimeSpent(enrollmentId, lessonId, minutes);
    }

    @Override
    public void deleteProgress(Integer id) {
        progressMapper.deleteProgress(id);
    }

    @Override
    public Integer getCompletedLessonCount(Integer enrollmentId) {
        return progressMapper.countCompletedLessons(enrollmentId);
    }

    @Override
    public Integer getCompletedLessonCountByCourse(Integer enrollmentId, Integer courseId) {
        return progressMapper.countCompletedLessonsByCourse(enrollmentId, courseId);
    }
}
