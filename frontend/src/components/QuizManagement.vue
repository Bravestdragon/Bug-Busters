<template>
  <div style="padding: 2rem; width: 100%;">
    <h2 style="color: #333; margin-bottom: 1.5rem; font-size: 1.8rem;">Manage Quizzes</h2>
    
    <div style="margin-bottom: 2rem; padding: 1.5rem; background: #f9f9f9; border-radius: 8px;">
      <h3 style="color: #555; margin-bottom: 1rem; font-size: 1.3rem;">Create New Quiz</h3>
      <form @submit.prevent="createQuiz" style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem;">
        <div style="display: flex; flex-direction: column;">
          <label style="font-weight: 500; margin-bottom: 0.5rem; color: #333;">Title:</label>
          <input v-model="newQuiz.title" type="text" required style="padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; font-family: inherit; font-size: 1rem;" />
        </div>
        
        <div style="display: flex; flex-direction: column;">
          <label style="font-weight: 500; margin-bottom: 0.5rem; color: #333;">Course ID:</label>
          <input v-model.number="newQuiz.courseId" type="number" required style="padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; font-family: inherit; font-size: 1rem;" />
        </div>
        
        <div style="display: flex; flex-direction: column; grid-column: 1 / -1;">
          <label style="font-weight: 500; margin-bottom: 0.5rem; color: #333;">Description:</label>
          <textarea v-model="newQuiz.description" required style="padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; font-family: inherit; font-size: 1rem; min-height: 100px; resize: vertical;"></textarea>
        </div>
        
        <div style="display: flex; flex-direction: column;">
          <label style="font-weight: 500; margin-bottom: 0.5rem; color: #333;">Time Limit (minutes):</label>
          <input v-model.number="newQuiz.timeLimit" type="number" required style="padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; font-family: inherit; font-size: 1rem;" />
        </div>
        
        <div style="display: flex; flex-direction: column;">
          <label style="font-weight: 500; margin-bottom: 0.5rem; color: #333;">Passing Score (%):</label>
          <input v-model.number="newQuiz.passingScore" type="number" min="0" max="100" style="padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; font-family: inherit; font-size: 1rem;" />
        </div>
        
        <button type="submit" style="grid-column: 1 / -1; padding: 0.75rem 1.5rem; background-color: #4caf50; color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: 500; font-size: 0.95rem;">Create Quiz</button>
      </form>
    </div>

    <div style="padding: 1.5rem; background: #f9f9f9; border-radius: 8px;">
      <h3 style="color: #555; margin-bottom: 1rem; font-size: 1.3rem;">All Quizzes</h3>
      <div v-if="loading" style="text-align: center; padding: 2rem; color: #999; font-size: 1.1rem;">Loading quizzes...</div>
      <div v-else-if="quizzes.length > 0" style="display: grid; gap: 1.5rem;">
        <div v-for="quiz in quizzes" :key="quiz.id" style="padding: 1.5rem; background: white; border: 1px solid #e0e0e0; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">
          <h4 style="margin: 0 0 0.5rem 0; color: #333; font-size: 1.2rem;">{{ quiz.title }}</h4>
          <p style="margin: 0.5rem 0 1rem 0; color: #666; line-height: 1.4;">{{ quiz.description }}</p>
          <div style="display: flex; gap: 1.5rem; font-size: 0.9rem; color: #666; margin-bottom: 1rem;">
            <span><strong>Course:</strong> {{ quiz.courseId }}</span>
            <span><strong>Time:</strong> {{ quiz.timeLimit }} min</span>
            <span><strong>Pass:</strong> {{ quiz.passingScore }}%</span>
          </div>
          <div style="display: flex; gap: 0.5rem;">
            <button @click="deleteQuiz(quiz.id)" style="padding: 0.5rem 1rem; background-color: #f44336; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 0.9rem;">Delete</button>
            <button v-if="quiz.status === 'DRAFT'" @click="publishQuiz(quiz.id)" style="padding: 0.5rem 1rem; background-color: #2196f3; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 0.9rem;">Publish</button>
          </div>
        </div>
      </div>
      <div v-else style="text-align: center; padding: 2rem; color: #999; font-size: 1.1rem;">No quizzes found. Create one to get started!</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { quizService } from '../services/api';

const quizzes = ref([]);
const loading = ref(false);
const newQuiz = ref({
  title: '',
  description: '',
  courseId: '',
  lessonId: '',
  timeLimit: 30,
  passingScore: 70,
  status: 'DRAFT'
});

onMounted(() => {
  loadQuizzes();
});

async function loadQuizzes() {
  loading.value = true;
  try {
    const { data } = await quizService.getAllQuizzes();
    quizzes.value = data;
  } catch (error) {
    console.error('Failed to load quizzes:', error);
    alert('Failed to load quizzes');
  } finally {
    loading.value = false;
  }
}

async function createQuiz() {
  try {
    await quizService.createQuiz(newQuiz.value);
    alert('Quiz created successfully!');
    newQuiz.value = {
      title: '',
      description: '',
      courseId: '',
      lessonId: '',
      timeLimit: 30,
      passingScore: 70,
      status: 'DRAFT'
    };
    await loadQuizzes();
  } catch (error) {
    console.error('Failed to create quiz:', error);
    alert(`Failed to create quiz: ${error.response?.data?.message || error.message}`);
  }
}

async function deleteQuiz(id) {
  if (!confirm('Are you sure you want to delete this quiz?')) return;
  
  try {
    await quizService.deleteQuiz(id);
    alert('Quiz deleted successfully');
    await loadQuizzes();
  } catch (error) {
    console.error('Failed to delete quiz:', error);
    alert(`Failed to delete quiz: ${error.response?.data?.message || error.message}`);
  }
}

async function publishQuiz(id) {
  try {
    await quizService.publishQuiz(id);
    alert('Quiz published successfully');
    await loadQuizzes();
  } catch (error) {
    console.error('Failed to publish quiz:', error);
    alert(`Failed to publish quiz: ${error.response?.data?.message || error.message}`);
  }
}
</script>
