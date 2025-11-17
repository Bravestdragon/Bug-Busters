<template>
  <div class="enrollment-section">
    <div v-if="isEnrolled" class="enrolled-status">
      <h3>✓ You are enrolled in this course</h3>
      <div class="enrollment-details">
        <p><strong>Status:</strong> {{ enrollment.enrollmentStatus }}</p>
        <p><strong>Enrolled Date:</strong> {{ formatDate(enrollment.enrolledDate) }}</p>
        <p><strong>Progress:</strong> {{ enrollment.progressPercentage }}%</p>
      </div>
      <button @click="dropCourse" class="btn-drop">Drop Course</button>
    </div>
    
    <div v-else class="enroll-form">
      <button @click="enrollInCourse" :disabled="enrolling" class="btn-enroll">
        {{ enrolling ? 'Enrolling...' : 'Enroll in This Course' }}
      </button>
      <p v-if="enrollmentError" class="error-message">{{ enrollmentError }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { enrollmentService } from '../services/api';
import authService from '../services/auth';

const props = defineProps({
  courseId: {
    type: Number,
    required: true
  },
  courseName: {
    type: String,
    required: true
  },
  onEnrollmentChange: {
    type: Function,
    default: () => {}
  }
});

const enrollment = ref(null);
const enrolling = ref(false);
const enrollmentError = ref('');

const isEnrolled = computed(() => enrollment.value !== null);

const loadEnrollmentStatus = async () => {
  try {
    const enrollments = await enrollmentService.getMyEnrollments();
    enrollment.value = enrollments.data.find(e => e.courseId === props.courseId) || null;
  } catch (error) {
    console.error('Failed to load enrollment status:', error);
  }
};

const enrollInCourse = async () => {
  enrolling.value = true;
  enrollmentError.value = '';
  
  try {
    const enrollmentData = {
      courseId: props.courseId,
      courseName: props.courseName
    };
    
    console.log('Enrolling with data:', enrollmentData);
    const response = await enrollmentService.enrollCourse(enrollmentData);
    enrollment.value = response.data;
    props.onEnrollmentChange();
  } catch (error) {
    console.error('Enrollment failed:', error);
    console.error('Error response:', error.response?.data);
    const errorMsg = typeof error.response?.data === 'string' 
      ? error.response.data 
      : JSON.stringify(error.response?.data) || error.message;
    enrollmentError.value = `Enrollment error: ${errorMsg}`;
  } finally {
    enrolling.value = false;
  }
};

const dropCourse = async () => {
  if (!enrollment.value || !confirm('Are you sure you want to drop this course?')) {
    return;
  }
  
  try {
    await enrollmentService.dropCourse(enrollment.value.id);
    enrollment.value = null;
    props.onEnrollmentChange();
  } catch (error) {
    console.error('Failed to drop course:', error);
    enrollmentError.value = 'Failed to drop course';
  }
};

const formatDate = (date) => {
  return date ? new Date(date).toLocaleDateString() : 'N/A';
};

// Load status on mount
loadEnrollmentStatus();
</script>

<style scoped>
.enrollment-section {
  margin: 20px 0;
  padding: 20px;
  border-radius: 8px;
  background-color: #f5f5f5;
}

.enrolled-status {
  background-color: #e8f5e9;
  border-left: 4px solid #4caf50;
  padding: 15px;
  border-radius: 4px;
}

.enrolled-status h3 {
  color: #2e7d32;
  margin-top: 0;
}

.enrollment-details {
  margin: 10px 0;
  font-size: 14px;
}

.enrollment-details p {
  margin: 5px 0;
}

.enroll-form {
  text-align: center;
}

.btn-enroll {
  background-color: #2196F3;
  color: white;
  padding: 12px 30px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-enroll:hover:not(:disabled) {
  background-color: #1976D2;
}

.btn-enroll:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.btn-drop {
  background-color: #f44336;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
  transition: background-color 0.3s;
}

.btn-drop:hover {
  background-color: #d32f2f;
}

.error-message {
  color: #f44336;
  margin-top: 10px;
}
</style>
