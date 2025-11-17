<template>
  <section>
    <h2>Courses</h2>
    
    <div class="filters">
      <input 
        v-model="searchKeyword" 
        placeholder="Search courses..." 
        @keyup.enter="performSearch"
        class="search-input"
      />
      <button @click="performSearch" class="btn-search">Search</button>
      
      <select v-model="selectedCategory" @change="filterByCategory" class="category-select">
        <option value="">All Categories</option>
        <option v-for="cat in categories" :key="cat" :value="cat">{{ cat }}</option>
      </select>

      <router-link to="/courses/create" class="btn-create">Create Course</router-link>
    </div>

    <div v-if="loading" class="loading">Loading courses...</div>
    <div v-else>
      <div v-if="courses.length" class="courses-grid">
        <div v-for="course in courses" :key="course.id" class="course-card">
          <div class="course-header">
            <h3>{{ course.title }}</h3>
            <span :class="['status', course.status.toLowerCase()]">{{ course.status }}</span>
          </div>
          <p class="course-description">{{ course.description }}</p>
          <div class="course-meta">
            <span class="category">{{ course.category }}</span>
            <span class="instructor">by {{ course.instructorName }}</span>
          </div>
          <div class="course-actions">
            <router-link :to="`/courses/${course.id}`" class="btn-view">View</router-link>
            <router-link v
              v-if="isAdmin || (currentUsername && currentUsername.toLowerCase() === course.instructorName.toLowerCase())" 
              :to="`/courses/${course.id}/edit`" 
              class="btn-edit"
            >Edit</router-link>
            <button 
              v-if="isAdmin || (currentUsername && currentUsername.toLowerCase() === course.instructorName.toLowerCase())" 
              @click="deleteCourse(course.id)" 
              class="btn-delete"
            >Delete</button>
          </div>
        </div>
      </div>
      <p v-else class="no-courses">No courses found.</p>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { courseService } from '../services/api';
import authService from '../services/auth';

const courses = ref([]);
const categories = ref([]);
const loading = ref(true);
const searchKeyword = ref('');
const selectedCategory = ref('');
const isAdmin = authService.isAdmin();
const currentUsername = authService.getUsername();

async function loadCourses() {
  loading.value = true;
  try {
    const { data } = await courseService.getAllCourses();
    courses.value = data;
  } catch (error) {
    console.error('Failed to load courses:', error);
    alert('Failed to load courses');
  } finally {
    loading.value = false;
  }
}

async function loadCategories() {
  try {
    const { data } = await courseService.getAllCategories();
    categories.value = data;
  } catch (error) {
    console.error('Failed to load categories:', error);
  }
}

async function performSearch() {
  if (!searchKeyword.value.trim()) {
    loadCourses();
    return;
  }
  
  loading.value = true;
  try {
    const { data } = await courseService.searchCourses(searchKeyword.value);
    courses.value = data;
  } catch (error) {
    console.error('Search failed:', error);
    alert('Search failed');
  } finally {
    loading.value = false;
  }
}

async function filterByCategory() {
  if (!selectedCategory.value) {
    loadCourses();
    return;
  }
  
  loading.value = true;
  try {
    const { data } = await courseService.getCoursesByCategory(selectedCategory.value);
    courses.value = data;
  } catch (error) {
    console.error('Filter failed:', error);
    alert('Filter failed');
  } finally {
    loading.value = false;
  }
}

async function deleteCourse(id) {
  if (!confirm('Are you sure you want to delete this course?')) return;
  
  try {
    await courseService.deleteCourse(id);
    courses.value = courses.value.filter(c => c.id !== id);
    alert('Course deleted successfully');
  } catch (error) {
    console.error('Delete failed:', error);
    alert(`Delete failed: ${error.response?.data || error.message}`);
  }
}

onMounted(() => {
  loadCourses();
  loadCategories();
});
</script>

<style scoped>
section {
  padding: 1rem;
}

.filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  align-items: center;
}

.search-input, .category-select {
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
}

.search-input {
  flex: 1;
  min-width: 200px;
}

.category-select {
  min-width: 150px;
}

.btn-search, .btn-create {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.btn-search {
  background-color: #1976d2;
  color: white;
}

.btn-search:hover {
  background-color: #1565c0;
}

.btn-create {
  background-color: #4caf50;
  color: white;
  text-decoration: none;
  display: inline-block;
}

.btn-create:hover {
  background-color: #45a049;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.courses-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.course-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1rem;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.5rem;
  gap: 1rem;
}

.course-header h3 {
  margin: 0;
  font-size: 1.1rem;
  color: #333;
}

.status {
  padding: 0.25rem 0.75rem;
  border-radius: 3px;
  font-size: 0.75rem;
  font-weight: bold;
  white-space: nowrap;
}

.status.draft {
  background-color: #fff3cd;
  color: #856404;
}

.status.published {
  background-color: #d4edda;
  color: #155724;
}

.status.archived {
  background-color: #e2e3e5;
  color: #383d41;
}

.course-description {
  color: #666;
  font-size: 0.95rem;
  margin: 0.5rem 0;
  flex-grow: 1;
}

.course-meta {
  display: flex;
  gap: 1rem;
  font-size: 0.85rem;
  color: #999;
  margin-bottom: 1rem;
}

.category {
  background-color: #e3f2fd;
  color: #1976d2;
  padding: 0.25rem 0.5rem;
  border-radius: 3px;
}

.course-actions {
  display: flex;
  gap: 0.5rem;
}

.btn-view, .btn-edit, .btn-delete {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  text-decoration: none;
  display: inline-block;
}

.btn-view {
  background-color: #1976d2;
  color: white;
}

.btn-view:hover {
  background-color: #1565c0;
}

.btn-edit {
  background-color: #ff9800;
  color: white;
}

.btn-edit:hover {
  background-color: #f57c00;
}

.btn-delete {
  background-color: #f44336;
  color: white;
}

.btn-delete:hover {
  background-color: #da190b;
}

.no-courses {
  text-align: center;
  padding: 2rem;
  color: #999;
}
</style>
