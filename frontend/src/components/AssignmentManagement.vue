<template>
  <div style="padding: 2rem; width: 100%;">
    <h2 style="color: #333; margin-bottom: 1.5rem; font-size: 1.8rem;">Manage Assignments</h2>
    
    <div style="margin-bottom: 2rem; padding: 1.5rem; background: #f9f9f9; border-radius: 8px;">
      <h3 style="color: #555; margin-bottom: 1rem; font-size: 1.3rem;">Create New Assignment</h3>
      <form @submit.prevent="createAssignment" style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem;">
        <div style="display: flex; flex-direction: column;">
          <label style="font-weight: 500; margin-bottom: 0.5rem; color: #333;">Title:</label>
          <input v-model="newAssignment.title" type="text" required style="padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; font-family: inherit; font-size: 1rem;" />
        </div>
        
        <div style="display: flex; flex-direction: column;">
          <label style="font-weight: 500; margin-bottom: 0.5rem; color: #333;">Course ID:</label>
          <input v-model.number="newAssignment.courseId" type="number" required style="padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; font-family: inherit; font-size: 1rem;" />
        </div>
        
        <div style="display: flex; flex-direction: column; grid-column: 1 / -1;">
          <label style="font-weight: 500; margin-bottom: 0.5rem; color: #333;">Description:</label>
          <textarea v-model="newAssignment.description" required style="padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; font-family: inherit; font-size: 1rem; min-height: 100px; resize: vertical;"></textarea>
        </div>
        
        <div style="display: flex; flex-direction: column;">
          <label style="font-weight: 500; margin-bottom: 0.5rem; color: #333;">Due Date:</label>
          <input v-model="newAssignment.dueDate" type="datetime-local" required style="padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; font-family: inherit; font-size: 1rem;" />
        </div>
        
        <div style="display: flex; flex-direction: column;">
          <label style="font-weight: 500; margin-bottom: 0.5rem; color: #333;">Max Score:</label>
          <input v-model.number="newAssignment.maxScore" type="number" required style="padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; font-family: inherit; font-size: 1rem;" />
        </div>
        
        <button type="submit" style="grid-column: 1 / -1; padding: 0.75rem 1.5rem; background-color: #4caf50; color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: 500; font-size: 0.95rem;">Create Assignment</button>
      </form>
    </div>

    <div style="padding: 1.5rem; background: #f9f9f9; border-radius: 8px;">
      <h3 style="color: #555; margin-bottom: 1rem; font-size: 1.3rem;">All Assignments</h3>
      <div v-if="loading" style="text-align: center; padding: 2rem; color: #999; font-size: 1.1rem;">Loading assignments...</div>
      <div v-else-if="assignments.length > 0" style="display: grid; gap: 1.5rem;">
        <div v-for="assignment in assignments" :key="assignment.id" style="padding: 1.5rem; background: white; border: 1px solid #e0e0e0; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">
          <h4 style="margin: 0 0 0.5rem 0; color: #333; font-size: 1.2rem;">{{ assignment.title }}</h4>
          <p style="margin: 0.5rem 0 1rem 0; color: #666; line-height: 1.4;">{{ assignment.description }}</p>
          <div style="display: flex; gap: 1.5rem; font-size: 0.9rem; color: #666; margin-bottom: 1rem;">
            <span><strong>Course:</strong> {{ assignment.courseId }}</span>
            <span><strong>Max Score:</strong> {{ assignment.maxScore }}</span>
          </div>
          <button @click="deleteAssignment(assignment.id)" style="padding: 0.5rem 1rem; background-color: #f44336; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 0.9rem;">Delete</button>
        </div>
      </div>
      <div v-else style="text-align: center; padding: 2rem; color: #999; font-size: 1.1rem;">No assignments found. Create one to get started!</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { assignmentService } from '../services/api';

const assignments = ref([]);
const loading = ref(false);
const newAssignment = ref({
  title: '',
  description: '',
  courseId: '',
  lessonId: '',
  dueDate: '',
  maxScore: 100,
  status: 'DRAFT'
});

onMounted(() => {
  loadAssignments();
});

async function loadAssignments() {
  loading.value = true;
  try {
    const { data } = await assignmentService.getAllAssignments();
    assignments.value = data;
  } catch (error) {
    console.error('Failed to load assignments:', error);
    alert('Failed to load assignments');
  } finally {
    loading.value = false;
  }
}

async function createAssignment() {
  try {
    await assignmentService.createAssignment(newAssignment.value);
    alert('Assignment created successfully!');
    newAssignment.value = {
      title: '',
      description: '',
      courseId: '',
      lessonId: '',
      dueDate: '',
      maxScore: 100,
      status: 'DRAFT'
    };
    await loadAssignments();
  } catch (error) {
    console.error('Failed to create assignment:', error);
    alert(`Failed to create assignment: ${error.response?.data?.message || error.message}`);
  }
}

async function deleteAssignment(id) {
  if (!confirm('Are you sure you want to delete this assignment?')) return;
  
  try {
    await assignmentService.deleteAssignment(id);
    alert('Assignment deleted successfully');
    await loadAssignments();
  } catch (error) {
    console.error('Failed to delete assignment:', error);
    alert(`Failed to delete assignment: ${error.response?.data?.message || error.message}`);
  }
}
</script>
