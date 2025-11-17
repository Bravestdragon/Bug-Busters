<template>
  <div class="lesson-view-container">
    <div v-if="!lesson" class="loading">
      <p>Loading lesson...</p>
    </div>
    <div v-else class="lesson-content">
      <div class="lesson-header">
        <button class="back-btn" @click="goBack">← Back</button>
        <h1>{{ lesson.title }}</h1>
        <div class="lesson-meta">
          <span>Duration: {{ lesson.durationMinutes }} minutes</span>
          <span>Status: {{ lesson.status }}</span>
        </div>
      </div>

      <div class="lesson-body">
        <div class="lesson-description">
          <h2>Description</h2>
          <p>{{ lesson.description }}</p>
        </div>

        <div v-if="lesson.videoUrl" class="lesson-video">
          <h2>Video</h2>
          <video width="100%" controls>
            <source :src="lesson.videoUrl" type="video/mp4">
            Your browser does not support the video tag.
          </video>
        </div>

        <div class="lesson-content-area">
          <h2>Content</h2>
          <div v-html="lesson.content" class="content-html"></div>
        </div>

        <div class="lesson-actions">
          <button @click="markAsComplete" class="btn btn-primary">
            {{ isCompleted ? 'Completed ✓' : 'Mark as Complete' }}
          </button>
          <button @click="viewQuizzes" class="btn btn-secondary">
            View Quizzes
          </button>
        </div>

        <div v-if="quizzes.length > 0" class="lesson-quizzes">
          <h2>Quizzes for this Lesson</h2>
          <div class="quiz-list">
            <div v-for="quiz in quizzes" :key="quiz.id" class="quiz-item">
              <h3>{{ quiz.title }}</h3>
              <p>{{ quiz.description }}</p>
              <p>Passing Score: {{ quiz.passingScore }}%</p>
              <button @click="startQuiz(quiz.id)" class="btn btn-info">
                Start Quiz
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { lessonService, lessonProgressService, quizService } from '../services/api';

export default {
  name: 'LessonView',
  data() {
    return {
      lesson: null,
      quizzes: [],
      isCompleted: false,
      error: null,
    };
  },
  computed: {
    lessonId() {
      return this.$route.params.lessonId;
    },
    courseId() {
      return this.$route.params.courseId;
    },
    enrollmentId() {
      return this.$route.params.enrollmentId;
    },
  },
  created() {
    this.fetchLesson();
    this.fetchQuizzes();
    this.checkProgress();
  },
  methods: {
    async fetchLesson() {
      try {
        const response = await lessonService.getLessonById(this.lessonId);
        this.lesson = response.data;
      } catch (error) {
        this.error = 'Failed to load lesson: ' + (error.response?.data || error.message);
        console.error(error);
      }
    },
    async fetchQuizzes() {
      try {
        const response = await quizService.getPublishedQuizzesByLesson(this.lessonId);
        this.quizzes = response.data;
      } catch (error) {
        console.error('Failed to fetch quizzes:', error);
      }
    },
    async checkProgress() {
      try {
        const response = await lessonProgressService.getEnrollmentProgress(this.enrollmentId);
        const progress = response.data.find(p => p.lessonId === parseInt(this.lessonId));
        if (progress) {
          this.isCompleted = progress.isCompleted;
        }
      } catch (error) {
        console.error('Failed to check progress:', error);
      }
    },
    async markAsComplete() {
      try {
        await lessonProgressService.markLessonComplete(this.lessonId, this.enrollmentId);
        this.isCompleted = true;
        this.$message.success('Lesson marked as complete!');
      } catch (error) {
        this.$message.error('Failed to mark lesson as complete');
        console.error(error);
      }
    },
    startQuiz(quizId) {
      this.$router.push({
        name: 'QuizTake',
        params: {
          quizId: quizId,
          courseId: this.courseId,
          enrollmentId: this.enrollmentId,
        },
      });
    },
    viewQuizzes() {
      if (this.quizzes.length > 0) {
        this.startQuiz(this.quizzes[0].id);
      }
    },
    goBack() {
      this.$router.back();
    },
  },
};
</script>

<style scoped>
.lesson-view-container {
  padding: 20px;
  max-width: 900px;
  margin: 0 auto;
}

.loading {
  text-align: center;
  padding: 40px;
  font-size: 18px;
}

.lesson-header {
  margin-bottom: 30px;
  border-bottom: 2px solid #007bff;
  padding-bottom: 20px;
}

.back-btn {
  background: none;
  border: none;
  color: #007bff;
  cursor: pointer;
  font-size: 16px;
  margin-bottom: 10px;
}

.back-btn:hover {
  color: #0056b3;
}

.lesson-header h1 {
  font-size: 28px;
  margin: 10px 0;
}

.lesson-meta {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #666;
}

.lesson-body {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
}

.lesson-description,
.lesson-video,
.lesson-content-area {
  margin-bottom: 30px;
}

.lesson-description h2,
.lesson-video h2,
.lesson-content-area h2 {
  font-size: 20px;
  margin-bottom: 15px;
  color: #333;
}

.content-html {
  line-height: 1.8;
  color: #555;
}

.lesson-video {
  margin: 30px 0;
}

.lesson-video video {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.lesson-actions {
  display: flex;
  gap: 15px;
  margin: 30px 0;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
}

.btn-primary {
  background-color: #28a745;
  color: white;
}

.btn-primary:hover {
  background-color: #218838;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background-color: #5a6268;
}

.btn-info {
  background-color: #007bff;
  color: white;
  padding: 8px 16px;
  font-size: 13px;
}

.btn-info:hover {
  background-color: #0056b3;
}

.lesson-quizzes {
  margin-top: 40px;
  padding-top: 30px;
  border-top: 2px solid #ddd;
}

.lesson-quizzes h2 {
  font-size: 20px;
  margin-bottom: 20px;
  color: #333;
}

.quiz-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.quiz-item {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.quiz-item h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.quiz-item p {
  margin: 8px 0;
  color: #666;
  font-size: 14px;
}
</style>
