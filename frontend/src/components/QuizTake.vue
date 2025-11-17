<template>
  <div class="quiz-container">
    <div v-if="!quiz" class="loading">
      <p>Loading quiz...</p>
    </div>
    <div v-else-if="quizSubmitted" class="quiz-result">
      <div class="result-header">
        <h1>Quiz Completed</h1>
        <div class="score-display">
          <div class="score-circle" :class="{ passed: isPassed, failed: !isPassed }">
            <span class="score-percentage">{{ Math.round(quizScore.percentage) }}%</span>
          </div>
        </div>
        <h2>{{ isPassed ? 'PASSED ✓' : 'NOT PASSED ✗' }}</h2>
        <p>Your Score: {{ quizScore.score }} / {{ quizScore.totalPoints }} points</p>
        <p>Passing Score: {{ quiz.passingScore }}%</p>
      </div>
      <div class="result-actions">
        <button @click="retakeQuiz" class="btn btn-primary">Retake Quiz</button>
        <button @click="goBack" class="btn btn-secondary">Back to Lesson</button>
      </div>
    </div>
    <div v-else class="quiz-content">
      <div class="quiz-header">
        <h1>{{ quiz.title }}</h1>
        <div class="quiz-info">
          <span>Question {{ currentQuestionIndex + 1 }} of {{ questions.length }}</span>
          <span v-if="quiz.timeLimit">Time Limit: {{ quiz.timeLimit }} minutes</span>
        </div>
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: progressPercentage + '%' }"></div>
        </div>
      </div>

      <div class="question-section" v-if="currentQuestion">
        <div class="question-text">
          <h2>{{ currentQuestion.questionText }}</h2>
        </div>

        <div class="question-options">
          <div v-if="currentQuestion.questionType === 'MULTIPLE_CHOICE'">
            <div v-for="option in questionOptions" :key="option.id" class="option">
              <label>
                <input
                  type="radio"
                  :value="option.id"
                  v-model="userAnswers[currentQuestion.id]"
                  @change="markAnswered"
                >
                <span class="option-text">{{ option.optionText }}</span>
              </label>
            </div>
          </div>

          <div v-else-if="currentQuestion.questionType === 'TRUE_FALSE'">
            <div class="option">
              <label>
                <input
                  type="radio"
                  value="true"
                  v-model="userAnswers[currentQuestion.id]"
                  @change="markAnswered"
                >
                <span class="option-text">True</span>
              </label>
            </div>
            <div class="option">
              <label>
                <input
                  type="radio"
                  value="false"
                  v-model="userAnswers[currentQuestion.id]"
                  @change="markAnswered"
                >
                <span class="option-text">False</span>
              </label>
            </div>
          </div>

          <div v-else-if="currentQuestion.questionType === 'SHORT_ANSWER'">
            <textarea
              v-model="userAnswers[currentQuestion.id]"
              @input="markAnswered"
              placeholder="Type your answer here..."
              rows="4"
              class="short-answer-input"
            ></textarea>
          </div>
        </div>
      </div>

      <div class="quiz-navigation">
        <button
          @click="previousQuestion"
          :disabled="currentQuestionIndex === 0"
          class="btn btn-secondary"
        >
          ← Previous
        </button>

        <div class="question-indicators">
          <button
            v-for="(q, index) in questions"
            :key="q.id"
            @click="goToQuestion(index)"
            :class="[
              'indicator',
              { active: index === currentQuestionIndex },
              { answered: userAnswers[q.id] !== undefined }
            ]"
          >
            {{ index + 1 }}
          </button>
        </div>

        <button
          v-if="currentQuestionIndex < questions.length - 1"
          @click="nextQuestion"
          class="btn btn-secondary"
        >
          Next →
        </button>
        <button
          v-else
          @click="submitQuiz"
          class="btn btn-success"
        >
          Submit Quiz
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { quizService, questionService, questionOptionService, quizAttemptService } from '../services/api';

export default {
  name: 'QuizTake',
  data() {
    return {
      quiz: null,
      questions: [],
      currentQuestionIndex: 0,
      userAnswers: {},
      questionOptions: {},
      quizSubmitted: false,
      quizScore: { score: 0, totalPoints: 0, percentage: 0 },
      attemptId: null,
      error: null,
    };
  },
  computed: {
    quizId() {
      return this.$route.params.quizId;
    },
    enrollmentId() {
      return this.$route.params.enrollmentId;
    },
    currentQuestion() {
      return this.questions[this.currentQuestionIndex];
    },
    progressPercentage() {
      return ((this.currentQuestionIndex + 1) / this.questions.length) * 100;
    },
    isPassed() {
      return this.quizScore.percentage >= (this.quiz?.passingScore || 70);
    },
  },
  async created() {
    await this.fetchQuiz();
    await this.fetchQuestions();
    await this.startAttempt();
  },
  methods: {
    async fetchQuiz() {
      try {
        const response = await quizService.getQuizById(this.quizId);
        this.quiz = response.data;
      } catch (error) {
        this.error = 'Failed to load quiz: ' + (error.response?.data || error.message);
        console.error(error);
      }
    },
    async fetchQuestions() {
      try {
        const response = await questionService.getQuestionsByQuiz(this.quizId);
        this.questions = response.data;
        
        for (const question of this.questions) {
          if (question.questionType === 'MULTIPLE_CHOICE') {
            const optionsResponse = await questionOptionService.getOptionsByQuestion(question.id);
            this.$set(this.questionOptions, question.id, optionsResponse.data);
          }
        }
      } catch (error) {
        this.error = 'Failed to load questions: ' + (error.response?.data || error.message);
        console.error(error);
      }
    },
    async startAttempt() {
      try {
        const attemptData = {
          quizId: this.quizId,
          enrollmentId: this.enrollmentId,
          score: 0,
          totalPoints: this.questions.reduce((sum, q) => sum + (q.points || 1), 0),
        };
        const response = await quizAttemptService.startQuizAttempt(attemptData);
        this.attemptId = response.data.id;
      } catch (error) {
        console.error('Failed to start quiz attempt:', error);
      }
    },
    async submitQuiz() {
      try {
        let score = 0;
        const totalPoints = this.quiz.totalQuestions;

        for (const question of this.questions) {
          const userAnswer = this.userAnswers[question.id];
          if (userAnswer !== undefined && userAnswer === question.correctAnswer) {
            score += question.points || 1;
          }
        }

        const percentage = (score / totalPoints) * 100;

        const attemptData = {
          score: score,
          totalPoints: totalPoints,
          percentage: percentage,
        };

        await quizAttemptService.submitQuizAttempt(this.attemptId, attemptData);

        this.quizScore = {
          score: score,
          totalPoints: totalPoints,
          percentage: percentage,
        };

        this.quizSubmitted = true;
        this.$message.success('Quiz submitted successfully!');
      } catch (error) {
        this.$message.error('Failed to submit quiz');
        console.error(error);
      }
    },
    nextQuestion() {
      if (this.currentQuestionIndex < this.questions.length - 1) {
        this.currentQuestionIndex++;
      }
    },
    previousQuestion() {
      if (this.currentQuestionIndex > 0) {
        this.currentQuestionIndex--;
      }
    },
    goToQuestion(index) {
      this.currentQuestionIndex = index;
    },
    markAnswered() {
      // Mark question as answered
    },
    retakeQuiz() {
      this.currentQuestionIndex = 0;
      this.userAnswers = {};
      this.quizSubmitted = false;
      this.startAttempt();
    },
    goBack() {
      this.$router.back();
    },
  },
};
</script>

<style scoped>
.quiz-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.loading,
.error {
  text-align: center;
  padding: 40px;
  font-size: 18px;
}

.quiz-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #007bff;
}

.quiz-header h1 {
  font-size: 24px;
  margin-bottom: 15px;
}

.quiz-info {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #666;
  margin-bottom: 15px;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background-color: #e9ecef;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background-color: #007bff;
  transition: width 0.3s ease;
}

.question-section {
  margin: 40px 0;
  padding: 30px;
  background: #f9f9f9;
  border-radius: 8px;
}

.question-text h2 {
  font-size: 18px;
  margin-bottom: 30px;
  color: #333;
  line-height: 1.6;
}

.question-options {
  margin: 20px 0;
}

.option {
  margin: 15px 0;
  padding: 15px;
  background: white;
  border: 2px solid #ddd;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.option:hover {
  border-color: #007bff;
  background-color: #f0f7ff;
}

.option label {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin: 0;
}

.option input[type="radio"] {
  margin-right: 15px;
  cursor: pointer;
  width: 18px;
  height: 18px;
}

.option-text {
  color: #333;
  font-size: 15px;
}

.short-answer-input {
  width: 100%;
  padding: 12px;
  border: 2px solid #ddd;
  border-radius: 4px;
  font-family: Arial, sans-serif;
  font-size: 14px;
}

.short-answer-input:focus {
  outline: none;
  border-color: #007bff;
}

.quiz-navigation {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  margin: 40px 0;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn-secondary:hover:not(:disabled) {
  background-color: #5a6268;
}

.btn-secondary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-success {
  background-color: #28a745;
  color: white;
}

.btn-success:hover {
  background-color: #218838;
}

.question-indicators {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: center;
  flex: 1;
}

.indicator {
  width: 40px;
  height: 40px;
  border: 2px solid #ddd;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-weight: bold;
  transition: all 0.3s ease;
}

.indicator:hover {
  border-color: #007bff;
}

.indicator.active {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}

.indicator.answered {
  border-color: #28a745;
}

.quiz-result {
  text-align: center;
  padding: 40px;
  background: #f9f9f9;
  border-radius: 8px;
}

.result-header {
  margin-bottom: 40px;
}

.result-header h1 {
  font-size: 32px;
  margin-bottom: 30px;
  color: #333;
}

.score-circle {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  font-size: 48px;
  font-weight: bold;
  color: white;
}

.score-circle.passed {
  background-color: #28a745;
}

.score-circle.failed {
  background-color: #dc3545;
}

.score-percentage {
  font-size: 48px;
}

.result-header h2 {
  font-size: 24px;
  margin: 20px 0;
}

.result-header p {
  font-size: 16px;
  color: #666;
  margin: 10px 0;
}

.result-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn-primary:hover {
  background-color: #0056b3;
}
</style>
