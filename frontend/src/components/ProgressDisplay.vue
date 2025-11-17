<template>
  <div class="progress-container">
    <div v-if="loading" class="loading">
      <p>Loading progress...</p>
    </div>
    <div v-else class="progress-content">
      <h1>Course Progress</h1>

      <div class="progress-overview">
        <div class="progress-card">
          <h3>Overall Progress</h3>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: overallProgress + '%' }"></div>
          </div>
          <p class="progress-text">{{ completedLessons }} / {{ totalLessons }} lessons completed</p>
        </div>

        <div class="stats-grid">
          <div class="stat-card">
            <span class="stat-label">Lessons Completed</span>
            <span class="stat-value">{{ completedLessons }}</span>
          </div>
          <div class="stat-card">
            <span class="stat-label">Quizzes Taken</span>
            <span class="stat-value">{{ totalQuizzes }}</span>
          </div>
          <div class="stat-card">
            <span class="stat-label">Time Spent</span>
            <span class="stat-value">{{ totalTimeSpent }} min</span>
          </div>
          <div class="stat-card">
            <span class="stat-label">Enrollment Status</span>
            <span class="stat-value">{{ enrollmentStatus }}</span>
          </div>
        </div>
      </div>

      <div class="lessons-progress">
        <h2>Lesson Progress</h2>
        <div class="lesson-list">
          <div v-for="lesson in lessons" :key="lesson.id" class="lesson-progress-item">
            <div class="lesson-info">
              <div class="lesson-header">
                <h3>{{ lesson.title }}</h3>
                <span v-if="isLessonCompleted(lesson.id)" class="badge badge-success">✓ Completed</span>
                <span v-else class="badge badge-warning">In Progress</span>
              </div>
              <p class="lesson-description">{{ lesson.description }}</p>
              <div class="lesson-meta">
                <span>Duration: {{ lesson.durationMinutes }} min</span>
                <span>Time Spent: {{ getTimeSpent(lesson.id) }} min</span>
              </div>
            </div>
            <div class="lesson-progress-bar">
              <div class="progress-fill" :style="{ width: isLessonCompleted(lesson.id) ? '100%' : '50%' }"></div>
            </div>
          </div>
        </div>
      </div>

      <div class="quiz-performance" v-if="quizAttempts.length > 0">
        <h2>Quiz Performance</h2>
        <div class="quiz-list">
          <div v-for="attempt in quizAttempts" :key="attempt.id" class="quiz-attempt-item">
            <div class="attempt-info">
              <h3>Quiz #{{ attempt.quizId }}</h3>
              <p>Score: {{ attempt.score }} / {{ attempt.totalPoints }}</p>
              <p>{{ Math.round(attempt.percentage) }}% - {{ attempt.percentage >= 70 ? 'PASSED' : 'FAILED' }}</p>
            </div>
            <div class="attempt-score">
              <div class="score-circle" :class="{ passed: attempt.percentage >= 70, failed: attempt.percentage < 70 }">
                {{ Math.round(attempt.percentage) }}%
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="action-buttons">
        <button @click="goBack" class="btn btn-secondary">Back to Course</button>
        <button @click="downloadReport" class="btn btn-primary">Download Report</button>
      </div>
    </div>
  </div>
</template>

<script>
import { lessonService, lessonProgressService, quizAttemptService } from '../services/api';

export default {
  name: 'ProgressDisplay',
  data() {
    return {
      loading: true,
      lessons: [],
      progress: [],
      quizAttempts: [],
      totalQuizzes: 0,
      totalTimeSpent: 0,
      enrollmentStatus: 'Active',
      error: null,
    };
  },
  computed: {
    courseId() {
      return this.$route.params.courseId;
    },
    enrollmentId() {
      return this.$route.params.enrollmentId;
    },
    totalLessons() {
      return this.lessons.length;
    },
    completedLessons() {
      return this.progress.filter(p => p.isCompleted).length;
    },
    overallProgress() {
      return this.totalLessons > 0 ? (this.completedLessons / this.totalLessons) * 100 : 0;
    },
  },
  async created() {
    await this.fetchData();
  },
  methods: {
    async fetchData() {
      try {
        // Fetch lessons
        const lessonsResponse = await lessonService.getLessonsByCourse(this.courseId);
        this.lessons = lessonsResponse.data;

        // Fetch progress
        const progressResponse = await lessonProgressService.getEnrollmentProgress(this.enrollmentId);
        this.progress = progressResponse.data;

        // Fetch quiz attempts
        const attemptsResponse = await quizAttemptService.getMyAttempts();
        this.quizAttempts = attemptsResponse.data.filter(a => 
          this.lessons.some(l => l.id === a.quizId) // Filter by course lessons
        );
        this.totalQuizzes = this.quizAttempts.length;

        // Calculate total time spent
        this.totalTimeSpent = this.progress.reduce((sum, p) => sum + (p.timeSpentMinutes || 0), 0);

        this.loading = false;
      } catch (error) {
        this.error = 'Failed to load progress data: ' + (error.response?.data || error.message);
        console.error(error);
        this.loading = false;
      }
    },
    isLessonCompleted(lessonId) {
      const progress = this.progress.find(p => p.lessonId === lessonId);
      return progress && progress.isCompleted;
    },
    getTimeSpent(lessonId) {
      const progress = this.progress.find(p => p.lessonId === lessonId);
      return progress ? progress.timeSpentMinutes : 0;
    },
    goBack() {
      this.$router.back();
    },
    downloadReport() {
      // Generate and download progress report as PDF or CSV
      const reportData = {
        course: this.courseId,
        totalLessons: this.totalLessons,
        completedLessons: this.completedLessons,
        overallProgress: this.overallProgress,
        totalTimeSpent: this.totalTimeSpent,
        quizzes: this.quizAttempts,
      };
      
      // Convert to CSV
      const csv = this.generateCSV(reportData);
      const element = document.createElement('a');
      element.setAttribute('href', 'data:text/csv;charset=utf-8,' + encodeURIComponent(csv));
      element.setAttribute('download', `progress-report-${this.courseId}.csv`);
      element.style.display = 'none';
      document.body.appendChild(element);
      element.click();
      document.body.removeChild(element);
    },
    generateCSV(data) {
      let csv = 'Progress Report\n\n';
      csv += `Course ID,${data.course}\n`;
      csv += `Total Lessons,${data.totalLessons}\n`;
      csv += `Completed Lessons,${data.completedLessons}\n`;
      csv += `Overall Progress,${data.overallProgress}%\n`;
      csv += `Total Time Spent,${data.totalTimeSpent} minutes\n\n`;
      csv += 'Quiz Attempts\n';
      csv += 'Quiz ID,Score,Total Points,Percentage,Status\n';
      data.quizzes.forEach(q => {
        csv += `${q.quizId},${q.score},${q.totalPoints},${q.percentage},${q.percentage >= 70 ? 'PASSED' : 'FAILED'}\n`;
      });
      return csv;
    },
  },
};
</script>

<style scoped>
.progress-container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.loading {
  text-align: center;
  padding: 40px;
  font-size: 18px;
}

.progress-content h1 {
  font-size: 28px;
  margin-bottom: 30px;
  color: #333;
}

.progress-overview {
  margin-bottom: 40px;
}

.progress-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.progress-card h3 {
  margin-bottom: 15px;
  color: #333;
}

.progress-bar {
  width: 100%;
  height: 25px;
  background-color: #e9ecef;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 10px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #007bff, #0056b3);
  transition: width 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 12px;
  font-weight: bold;
}

.progress-text {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 20px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  border-left: 4px solid #007bff;
}

.stat-label {
  display: block;
  font-size: 12px;
  color: #666;
  margin-bottom: 10px;
  text-transform: uppercase;
}

.stat-value {
  display: block;
  font-size: 28px;
  font-weight: bold;
  color: #007bff;
}

.lessons-progress {
  margin-bottom: 40px;
}

.lessons-progress h2 {
  font-size: 20px;
  margin-bottom: 20px;
  color: #333;
}

.lesson-list {
  display: grid;
  gap: 15px;
}

.lesson-progress-item {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.lesson-info {
  flex: 1;
}

.lesson-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 10px;
}

.lesson-header h3 {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
}

.badge-success {
  background-color: #d4edda;
  color: #155724;
}

.badge-warning {
  background-color: #fff3cd;
  color: #856404;
}

.lesson-description {
  font-size: 14px;
  color: #666;
  margin: 10px 0;
}

.lesson-meta {
  display: flex;
  gap: 20px;
  font-size: 12px;
  color: #999;
}

.lesson-progress-bar {
  width: 150px;
  height: 20px;
  background-color: #e9ecef;
  border-radius: 4px;
  overflow: hidden;
  margin-left: 20px;
}

.quiz-performance {
  margin-bottom: 40px;
}

.quiz-performance h2 {
  font-size: 20px;
  margin-bottom: 20px;
  color: #333;
}

.quiz-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 15px;
}

.quiz-attempt-item {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.attempt-info h3 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 16px;
}

.attempt-info p {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
}

.score-circle {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
  color: white;
  margin-left: 20px;
}

.score-circle.passed {
  background-color: #28a745;
}

.score-circle.failed {
  background-color: #dc3545;
}

.action-buttons {
  display: flex;
  gap: 15px;
  justify-content: center;
  margin-top: 40px;
}

.btn {
  padding: 12px 24px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background-color: #5a6268;
}
</style>
