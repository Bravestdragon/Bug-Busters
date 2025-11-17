<template>
  <section>
    <h1>Quizzes</h1>
    
    <div class="filters">
      <input 
        v-model="searchQuery" 
        type="text" 
        placeholder="Search quizzes..." 
        class="search-input"
      />
      <button @click="searchQuizzes" class="btn-search">Search</button>
      <select v-model="filterCourse" class="filter-select">
        <option value="">All Courses</option>
        <option v-for="course in courses" :key="course.id" :value="course.id">
          {{ course.title }}
        </option>
      </select>
    </div>

    <div v-if="loading" class="loading">Loading quizzes...</div>
    <div v-else-if="quizzes.length > 0" class="quizzes-grid">
      <div v-for="quiz in quizzes" :key="quiz.id" class="quiz-card">
        <h3>{{ quiz.title }}</h3>
        <p class="description">{{ quiz.description }}</p>
        <div class="quiz-meta">
          <span><strong>Questions:</strong> {{ quiz.questionCount || 0 }}</span>
          <span><strong>Duration:</strong> {{ quiz.timeLimit }} min</span>
        </div>
        <div v-if="quiz.courseName" class="course-badge">
          {{ quiz.courseName }}
        </div>
        <button @click="takeQuiz(quiz.id)" class="btn-take">Take Quiz</button>
      </div>
    </div>
    <div v-else class="no-data">No quizzes found</div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const quizzes = ref([]);
const courses = ref([]);
const loading = ref(true);
const searchQuery = ref('');
const filterCourse = ref('');

const API_BASE = 'http://localhost:8080/api';

onMounted(async () => {
  await loadQuizzes();
  await loadCourses();
});

async function loadQuizzes() {
  try {
    const token = localStorage.getItem('token');
    const config = token ? { headers: { Authorization: `Bearer ${token}` } } : {};
    
    const { data } = await axios.get(`${API_BASE}/quizzes`, config);
    quizzes.value = data;
  } catch (error) {
    console.error('Failed to load quizzes:', error);
  } finally {
    loading.value = false;
  }
}

async function loadCourses() {
  try {
    const { data } = await axios.get(`${API_BASE}/courses`);
    courses.value = data;
  } catch (error) {
    console.error('Failed to load courses:', error);
  }
}

async function searchQuizzes() {
  try {
    loading.value = true;
    const token = localStorage.getItem('token');
    const config = token ? { headers: { Authorization: `Bearer ${token}` } } : {};
    
    let url = `${API_BASE}/quizzes`;
    const params = new URLSearchParams();
    
    if (searchQuery.value) {
      params.append('title', searchQuery.value);
    }
    if (filterCourse.value) {
      params.append('courseId', filterCourse.value);
    }
    
    if (params.toString()) {
      url += `?${params.toString()}`;
    }
    
    const { data } = await axios.get(url, config);
    quizzes.value = data;
  } catch (error) {
    console.error('Search failed:', error);
    alert('Failed to search quizzes');
  } finally {
    loading.value = false;
  }
}

function takeQuiz(quizId) {
  router.push(`/quiz/${quizId}`);
}
</script>

<style scoped>
section {
  padding: 2rem;
  max-width: 1000px;
  margin: 0 auto;
}

h1 {
  color: #333;
  margin-bottom: 2rem;
}

.filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.search-input,
.filter-select {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.search-input {
  flex: 1;
  min-width: 200px;
}

.btn-search {
  padding: 0.75rem 1.5rem;
  background-color: #1976d2;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.btn-search:hover {
  background-color: #1565c0;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.quizzes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
}

.quiz-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 1.5rem;
  background: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.quiz-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.quiz-card h3 {
  margin: 0 0 0.5rem 0;
  color: #333;
  font-size: 1.2rem;
}

.description {
  color: #666;
  margin: 0.5rem 0 1rem 0;
  font-size: 0.9rem;
}

.quiz-meta {
  display: flex;
  gap: 1rem;
  font-size: 0.85rem;
  color: #666;
  margin-bottom: 1rem;
}

.quiz-meta span {
  display: flex;
  gap: 0.5rem;
}

.course-badge {
  display: inline-block;
  background-color: #e3f2fd;
  color: #1976d2;
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.8rem;
  margin-bottom: 1rem;
}

.btn-take {
  width: 100%;
  padding: 0.75rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
}

.btn-take:hover {
  background-color: #45a049;
}

.no-data {
  text-align: center;
  padding: 2rem;
  color: #999;
}
</style>
