<template>
  <div class="my-enrollments">
    <h2>My Courses</h2>

    <div v-if="loading" class="loading">Loading courses...</div>
    
    <div v-else-if="enrollments.length === 0" class="no-enrollments">
      <p>You are not enrolled in any courses yet.</p>
      <router-link to="/courses" class="link">Browse courses</router-link>
    </div>

    <div v-else class="enrollments-grid">
      <div v-for="enrollment in enrollments" :key="enrollment.id" class="enrollment-card">
        <div class="card-header">
          <h3>{{ enrollment.courseName }}</h3>
          <span class="status" :class="enrollment.enrollmentStatus.toLowerCase()">
            {{ enrollment.enrollmentStatus }}
          </span>
        </div>

        <div class="card-body">
          <div class="progress-section">
            <p><strong>Progress:</strong></p>
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: enrollment.progressPercentage + '%' }"></div>
            </div>
            <p class="progress-text">{{ enrollment.progressPercentage }}%</p>
          </div>

          <div class="dates-section">
            <p><strong>Enrolled:</strong> {{ formatDate(enrollment.enrolledDate) }}</p>
            <p v-if="enrollment.completedDate">
              <strong>Completed:</strong> {{ formatDate(enrollment.completedDate) }}
            </p>
          </div>
        </div>

        <div class="card-actions">
          <button @click="viewCourse(enrollment.courseId)" class="btn-view">
            View Course
          </button>
          <button @click="dropCourse(enrollment.id)" class="btn-drop">
            Drop
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { enrollmentService } from '../services/api';

const router = useRouter();
const enrollments = ref([]);
const loading = ref(true);

const loadEnrollments = async () => {
  loading.value = true;
  try {
    const response = await enrollmentService.getMyEnrollments();
    enrollments.value = response.data || [];
  } catch (error) {
    console.error('Failed to load enrollments:', error);
  } finally {
    loading.value = false;
  }
};

const viewCourse = (courseId) => {
  router.push(`/courses/${courseId}`);
};

const dropCourse = async (enrollmentId) => {
  if (!confirm('Are you sure you want to drop this course?')) {
    return;
  }

  try {
    await enrollmentService.dropCourse(enrollmentId);
    enrollments.value = enrollments.value.filter(e => e.id !== enrollmentId);
  } catch (error) {
    console.error('Failed to drop course:', error);
    alert('Failed to drop course');
  }
};

const formatDate = (date) => {
  return date ? new Date(date).toLocaleDateString() : 'N/A';
};

onMounted(() => {
  loadEnrollments();
});
</script>

<style scoped>
.my-enrollments {
  padding: 20px;
}

.my-enrollments h2 {
  margin-bottom: 20px;
}

.loading {
  text-align: center;
  padding: 40px;
  color: #666;
}

.no-enrollments {
  text-align: center;
  padding: 40px;
  background-color: #f5f5f5;
  border-radius: 8px;
}

.no-enrollments p {
  margin-bottom: 20px;
  color: #666;
}

.link {
  color: #2196F3;
  text-decoration: none;
  padding: 10px 20px;
  border: 1px solid #2196F3;
  border-radius: 4px;
  display: inline-block;
}

.link:hover {
  background-color: #2196F3;
  color: white;
}

.enrollments-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.enrollment-card {
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.card-header {
  padding: 15px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #ddd;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
}

.status {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  text-transform: uppercase;
}

.status.active {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.status.completed {
  background-color: #e3f2fd;
  color: #1565c0;
}

.status.dropped {
  background-color: #ffebee;
  color: #c62828;
}

.card-body {
  padding: 15px;
  flex: 1;
}

.progress-section {
  margin-bottom: 15px;
}

.progress-section p {
  margin: 5px 0;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background-color: #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
  margin: 5px 0;
}

.progress-fill {
  height: 100%;
  background-color: #4caf50;
  transition: width 0.3s;
}

.progress-text {
  font-size: 12px;
  color: #666;
  margin: 5px 0 0 0;
}

.dates-section {
  border-top: 1px solid #eee;
  padding-top: 10px;
}

.dates-section p {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
}

.card-actions {
  padding: 15px;
  background-color: #f9f9f9;
  border-top: 1px solid #eee;
  display: flex;
  gap: 10px;
}

.btn-view {
  flex: 1;
  background-color: #2196F3;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-view:hover {
  background-color: #1976D2;
}

.btn-drop {
  flex: 1;
  background-color: #f44336;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-drop:hover {
  background-color: #d32f2f;
}
</style>
