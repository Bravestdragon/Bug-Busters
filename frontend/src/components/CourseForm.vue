<template>
  <section>
    <h2>{{ isEdit ? 'Edit Course' : 'Create Course' }}</h2>
    <form @submit.prevent="submit" class="course-form">
      <div class="form-group">
        <label>Course Title
          <input v-model="form.title" placeholder="Enter course title" required />
        </label>
      </div>

      <div class="form-group">
        <label>Description
          <textarea 
            v-model="form.description" 
            placeholder="Enter course description" 
            rows="5"
            required
          ></textarea>
        </label>
      </div>

      <div class="form-group">
        <label>Category
          <select v-model="form.category" required>
            <option value="">Select a category</option>
            <option v-for="cat in categories" :key="cat" :value="cat">{{ cat }}</option>
            <option value="Mathematics">Mathematics</option>
            <option value="Science">Science</option>
            <option value="Language">Language</option>
            <option value="Programming">Programming</option>
            <option value="Arts">Arts</option>
            <option value="Other">Other</option>
          </select>
        </label>
      </div>

      <div class="form-group">
        <label>Course Status
          <select v-model="form.status">
            <option value="DRAFT">Draft (Not visible to students)</option>
            <option value="PUBLISHED">Published (Visible to students)</option>
            <option value="ARCHIVED">Archived</option>
          </select>
        </label>
      </div>

      <div class="form-actions">
        <button type="submit" class="btn-save">{{ isEdit ? 'Update' : 'Create' }} Course</button>
        <router-link to="/courses" class="btn-cancel">Cancel</router-link>
      </div>
      <p v-if="message" :class="messageStatus">{{ message }}</p>
    </form>
  </section>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { courseService } from '../services/api';
import authService from '../services/auth';

const route = useRoute();
const router = useRouter();
const form = ref({ title: '', description: '', category: '', status: 'DRAFT' });
const categories = ref([]);
const message = ref('');
const messageStatus = ref('');

const isEdit = computed(() => !!route.params.id);
const isLoggedIn = authService.isLoggedIn();

onMounted(async () => {
  // Check authentication
  if (!isLoggedIn) {
    router.push('/login');
    return;
  }

  // Load categories
  try {
    const { data } = await courseService.getAllCategories();
    categories.value = data;
  } catch (error) {
    console.error('Failed to load categories:', error);
  }

  // Load course if editing
  if (isEdit.value) {
    try {
      const { data } = await courseService.getCourseById(route.params.id);
      
      // Check authorization
      const currentUsername = authService.getUsername();
      const isAdmin = authService.isAdmin();
      const isInstructor = currentUsername && currentUsername.toLowerCase() === data.instructorName.toLowerCase();

      if (!isInstructor && !isAdmin) {
        alert('You are not authorized to edit this course');
        router.push('/courses');
        return;
      }

      form.value = { ...data };
    } catch (error) {
      console.error('Failed to load course:', error);
      alert('Failed to load course data');
      router.push('/courses');
    }
  }
});

async function submit() {
  message.value = '';
  try {
    if (isEdit.value) {
      await courseService.updateCourse(route.params.id, form.value);
      message.value = 'Course updated successfully!';
      messageStatus.value = 'success';
      setTimeout(() => router.push('/courses'), 1500);
    } else {
      await courseService.createCourse(form.value);
      message.value = 'Course created successfully!';
      messageStatus.value = 'success';
      setTimeout(() => router.push('/courses'), 1500);
    }
  } catch (error) {
    message.value = `Error: ${error.response?.data || error.message}`;
    messageStatus.value = 'error';
    console.error('Save failed:', error);
  }
}
</script>

<style scoped>
section {
  padding: 1rem;
  max-width: 600px;
  margin: 0 auto;
}

.course-form {
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  font-weight: 500;
  color: #333;
}

input, textarea, select {
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
  font-family: inherit;
}

input:focus, textarea:focus, select:focus {
  outline: none;
  border-color: #1976d2;
  box-shadow: 0 0 5px rgba(25, 118, 210, 0.3);
}

textarea {
  resize: vertical;
}

.form-actions {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
}

.btn-save, .btn-cancel {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  text-decoration: none;
  display: inline-block;
}

.btn-save {
  background-color: #4caf50;
  color: white;
  flex: 1;
}

.btn-save:hover {
  background-color: #45a049;
}

.btn-cancel {
  background-color: #e0e0e0;
  color: #333;
  flex: 1;
  text-align: center;
}

.btn-cancel:hover {
  background-color: #d0d0d0;
}

.success {
  color: #4caf50;
  padding: 0.75rem;
  background-color: #e8f5e9;
  border-radius: 4px;
  margin-top: 1rem;
}

.error {
  color: #f44336;
  padding: 0.75rem;
  background-color: #ffebee;
  border-radius: 4px;
  margin-top: 1rem;
}
</style>
