<template>
  <section>
    <router-link to="/courses" class="btn-back">← Back to Courses</router-link>
    
    <div v-if="loading" class="loading">Loading course...</div>
    <div v-else-if="course" class="course-detail">
      <div class="course-header">
        <h1>{{ course.title }}</h1>
        <span :class="['status', course.status.toLowerCase()]">{{ course.status }}</span>
      </div>

      <div class="course-meta">
        <div class="meta-item">
          <strong>Instructor:</strong> {{ course.instructorName }}
        </div>
        <div class="meta-item">
          <strong>Category:</strong> 
          <span class="category">{{ course.category }}</span>
        </div>
        <div class="meta-item">
          <strong>Students Enrolled:</strong> {{ course.enrollmentCount || 0 }}
        </div>
        <div class="meta-item">
          <strong>Created:</strong> {{ formatDate(course.createdDate) }}
        </div>
      </div>

      <div class="course-content">
        <h2>Course Description</h2>
        <p>{{ course.description }}</p>
      </div>

      <!-- Enrollment Section -->
      <Enrollment 
        :courseId="parseInt(route.params.id)" 
        :courseName="course.title"
        @onEnrollmentChange="refreshEnrollmentCount"
      />

      <div class="course-actions">
        <router-link 
          v-if="isAdmin || isInstructor" 
          :to="`/courses/${course.id}/edit`" 
          class="btn-edit"
        >Edit Course</router-link>
        <button 
          v-if="isAdmin || isInstructor" 
          @click="deleteCourse" 
          class="btn-delete"
        >Delete Course</button>
      </div>
    </div>
    <div v-else class="not-found">Course not found</div>
  </section>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { courseService, enrollmentService } from '../services/api';
import authService from '../services/auth';
import Enrollment from './Enrollment.vue';

const route = useRoute();
const router = useRouter();
const course = ref(null);
const loading = ref(true);

const currentUsername = authService.getUsername();
const isAdmin = authService.isAdmin();
const isInstructor = computed(() => 
  currentUsername && course.value && currentUsername.toLowerCase() === course.value.instructorName.toLowerCase()
);

onMounted(async () => {
  try {
    const { data } = await courseService.getCourseById(route.params.id);
    course.value = data;
  } catch (error) {
    console.error('Failed to load course:', error);
    alert('Failed to load course');
  } finally {
    loading.value = false;
  }
});

function formatDate(dateString) {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', { 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric' 
  });
}

async function deleteCourse() {
  if (!confirm('Are you sure you want to delete this course?')) return;
  
  try {
    await courseService.deleteCourse(route.params.id);
    alert('Course deleted successfully');
    router.push('/courses');
  } catch (error) {
    console.error('Delete failed:', error);
    alert(`Delete failed: ${error.response?.data || error.message}`);
  }
}

const refreshEnrollmentCount = async () => {
  try {
    const count = await enrollmentService.getEnrollmentCount(route.params.id);
    if (course.value) {
      course.value.enrollmentCount = count.data;
    }
  } catch (error) {
    console.error('Failed to refresh enrollment count:', error);
  }
};
</script>

<style scoped>
section {
  padding: 1rem;
  max-width: 800px;
  margin: 0 auto;
}

.btn-back {
  display: inline-block;
  margin-bottom: 1rem;
  color: #1976d2;
  text-decoration: none;
  font-weight: 500;
}

.btn-back:hover {
  text-decoration: underline;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.not-found {
  text-align: center;
  padding: 2rem;
  color: #999;
}

.course-detail {
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1rem;
  margin-bottom: 1.5rem;
  border-bottom: 2px solid #f0f0f0;
  padding-bottom: 1rem;
}

.course-header h1 {
  margin: 0;
  color: #333;
}

.status {
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-size: 0.85rem;
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

.course-meta {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1rem;
  margin-bottom: 2rem;
  padding: 1rem;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.meta-item {
  color: #666;
}

.meta-item strong {
  color: #333;
  display: block;
  margin-bottom: 0.25rem;
}

.category {
  display: inline-block;
  background-color: #e3f2fd;
  color: #1976d2;
  padding: 0.25rem 0.75rem;
  border-radius: 3px;
  font-size: 0.9rem;
}

.course-content {
  margin-bottom: 2rem;
}

.course-content h2 {
  color: #333;
  margin-top: 0;
}

.course-content p {
  color: #666;
  line-height: 1.6;
}

.course-actions {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.btn-edit, .btn-delete, .btn-enroll {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  text-decoration: none;
  display: inline-block;
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

.btn-enroll {
  background-color: #4caf50;
  color: white;
}

.btn-enroll:hover {
  background-color: #45a049;
}
</style>
