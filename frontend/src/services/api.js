import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api'; // Add /api here

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 
    'Content-Type': 'application/json'
  },
  timeout: 10000
});

// Request interceptor
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    console.log('API Request:', config.method?.toUpperCase(), config.url);
    return config;
  },
  error => {
    console.error('Request Error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  response => {
    console.log('API Response:', response.status, response.data);
    return response;
  },
  error => {
    console.error('API Error:', error.response?.status, error.response?.data);
    
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    
    return Promise.reject(error);
  }
);

// User service methods
export const userService = {
  register: (userData) => api.post('/users/register', userData),
  login: (credentials) => api.post('/users/login', credentials),
  getUsers: () => api.get('/users'),
  getUserById: (id) => api.get(`/users/${id}`),
  updateUser: (id, userData) => api.put(`/users/${id}`, userData),
  deleteUser: (id) => api.delete(`/users/${id}`),
};

// Course service methods
export const courseService = {
  getAllCourses: () => api.get('/courses'),
  getCourseById: (id) => api.get(`/courses/${id}`),
  searchCourses: (keyword) => api.get('/courses/search', { params: { keyword } }),
  getCoursesByCategory: (category) => api.get(`/courses/category/${category}`),
  getAllCategories: () => api.get('/courses/meta/categories'),
  createCourse: (courseData) => api.post('/courses', courseData),
  updateCourse: (id, courseData) => api.put(`/courses/${id}`, courseData),
  deleteCourse: (id) => api.delete(`/courses/${id}`),
  getMyCourses: () => api.get('/courses/my-courses'),
};

// Enrollment service methods
export const enrollmentService = {
  enrollCourse: (enrollmentData) => api.post('/enrollments', enrollmentData),
  getEnrollmentById: (id) => api.get(`/enrollments/${id}`),
  getEnrollmentsByCourse: (courseId) => api.get(`/enrollments/course/${courseId}`),
  getMyEnrollments: () => api.get('/enrollments/student/my-courses'),
  getAllEnrollments: () => api.get('/enrollments'),
  updateEnrollmentStatus: (id, status) => api.put(`/enrollments/${id}/status`, null, { params: { status } }),
  updateProgressPercentage: (id, progress) => api.put(`/enrollments/${id}/progress`, null, { params: { progress } }),
  dropCourse: (id) => api.delete(`/enrollments/${id}`),
  getEnrollmentCount: (courseId) => api.get(`/enrollments/course/${courseId}/count`),
};

// Lesson service methods
export const lessonService = {
  getLessonsByCourse: (courseId) => api.get(`/lessons/course/${courseId}`),
  getPublishedLessonsByCourse: (courseId) => api.get(`/lessons/course/${courseId}/published`),
  getLessonById: (id) => api.get(`/lessons/${id}`),
  createLesson: (lessonData) => api.post('/lessons', lessonData),
  updateLesson: (id, lessonData) => api.put(`/lessons/${id}`, lessonData),
  publishLesson: (id) => api.put(`/lessons/${id}/publish`),
  deleteLesson: (id) => api.delete(`/lessons/${id}`),
  getLessonCount: (courseId) => api.get(`/lessons/course/${courseId}/count`),
};

// Lesson Progress service methods
export const lessonProgressService = {
  getCourseProgress: (courseId) => api.get(`/lesson-progress/course/${courseId}`),
  getEnrollmentProgress: (enrollmentId) => api.get(`/lesson-progress/enrollment/${enrollmentId}`),
  getLessonProgress: (lessonId) => api.get(`/lesson-progress/lesson/${lessonId}`),
  createProgress: (progressData) => api.post('/lesson-progress', progressData),
  markLessonComplete: (lessonId, enrollmentId) => api.put(`/lesson-progress/lesson/${lessonId}/complete`, null, { params: { enrollmentId } }),
  updateTimeSpent: (lessonId, enrollmentId, minutes) => api.put(`/lesson-progress/lesson/${lessonId}/time`, null, { params: { enrollmentId, minutes } }),
  getCompletedCount: (enrollmentId) => api.get('/lesson-progress/completed/count', { params: { enrollmentId } }),
  deleteProgress: (id) => api.delete(`/lesson-progress/${id}`),
};

// Quiz service methods
export const quizService = {
  getAllQuizzes: () => api.get('/quizzes'),
  getQuizzesByCourse: (courseId) => api.get(`/quizzes/course/${courseId}`),
  getPublishedQuizzesByCourse: (courseId) => api.get(`/quizzes/course/${courseId}/published`),
  getQuizzesByLesson: (lessonId) => api.get(`/quizzes/lesson/${lessonId}`),
  getPublishedQuizzesByLesson: (lessonId) => api.get(`/quizzes/lesson/${lessonId}/published`),
  getQuizById: (id) => api.get(`/quizzes/${id}`),
  createQuiz: (quizData) => api.post('/quizzes', quizData),
  updateQuiz: (id, quizData) => api.put(`/quizzes/${id}`, quizData),
  publishQuiz: (id) => api.put(`/quizzes/${id}/publish`),
  deleteQuiz: (id) => api.delete(`/quizzes/${id}`),
  getQuizCount: (courseId) => api.get(`/quizzes/course/${courseId}/count`),
};

// Question service methods
export const questionService = {
  getQuestionsByQuiz: (quizId) => api.get(`/questions/quiz/${quizId}`),
  getQuestionById: (id) => api.get(`/questions/${id}`),
  createQuestion: (questionData) => api.post('/questions', questionData),
  updateQuestion: (id, questionData) => api.put(`/questions/${id}`, questionData),
  deleteQuestion: (id) => api.delete(`/questions/${id}`),
  getQuestionCount: (quizId) => api.get(`/questions/quiz/${quizId}/count`),
};

// Question Option service methods
export const questionOptionService = {
  getOptionsByQuestion: (questionId) => api.get(`/question-options/question/${questionId}`),
  getOptionById: (id) => api.get(`/question-options/${id}`),
  createOption: (optionData) => api.post('/question-options', optionData),
  updateOption: (id, optionData) => api.put(`/question-options/${id}`, optionData),
  deleteOption: (id) => api.delete(`/question-options/${id}`),
};

// Quiz Attempt service methods
export const quizAttemptService = {
  startQuizAttempt: (attemptData) => api.post('/quiz-attempts', attemptData),
  submitQuizAttempt: (id, attemptData) => api.put(`/quiz-attempts/${id}/submit`, attemptData),
  getAttemptById: (id) => api.get(`/quiz-attempts/${id}`),
  getAttemptsByQuiz: (quizId) => api.get(`/quiz-attempts/quiz/${quizId}`),
  getMyAttempts: () => api.get('/quiz-attempts/student/my-attempts'),
  getAttemptsByQuizAndStudent: (quizId) => api.get(`/quiz-attempts/quiz/${quizId}/student`),
  deleteAttempt: (id) => api.delete(`/quiz-attempts/${id}`),
};

// Assignment service methods
export const assignmentService = {
  getAllAssignments: () => api.get('/assignments'),
  getAssignmentsByCourse: (courseId) => api.get(`/assignments/course/${courseId}`),
  getAssignmentsByLesson: (lessonId) => api.get(`/assignments/lesson/${lessonId}`),
  getAssignmentById: (id) => api.get(`/assignments/${id}`),
  getActiveAssignments: () => api.get('/assignments/active'),
  createAssignment: (assignmentData) => api.post('/assignments', assignmentData),
  updateAssignment: (id, assignmentData) => api.put(`/assignments/${id}`, assignmentData),
  deleteAssignment: (id) => api.delete(`/assignments/${id}`),
  getAssignmentCount: (courseId) => api.get(`/assignments/course/${courseId}/count`),
};

// Assignment Submission service methods
export const assignmentSubmissionService = {
  submitAssignment: (submissionData) => api.post('/assignment-submissions', submissionData),
  getSubmissionById: (id) => api.get(`/assignment-submissions/${id}`),
  getSubmissionsByAssignment: (assignmentId) => api.get(`/assignment-submissions/assignment/${assignmentId}`),
  getSubmissionsByStudent: (userId) => api.get(`/assignment-submissions/student/${userId}`),
  getStudentSubmission: (assignmentId, userId) => api.get(`/assignment-submissions/assignment/${assignmentId}/student/${userId}`),
  gradeSubmission: (id, gradeData) => api.put(`/assignment-submissions/${id}/grade`, gradeData),
  updateSubmission: (id, submissionData) => api.put(`/assignment-submissions/${id}`, submissionData),
  deleteSubmission: (id) => api.delete(`/assignment-submissions/${id}`),
  getPendingGrades: () => api.get('/assignment-submissions/pending-grades'),
};

export default api;