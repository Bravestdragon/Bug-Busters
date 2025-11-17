<template>
  <div class="assignments-container">
    <div class="assignments-header">
      <h2>Assignments</h2>
      <button v-if="isAdmin" @click="showCreateModal = true" class="btn btn-primary">
        Create Assignment
      </button>
    </div>

    <!-- Filters -->
    <div class="filters">
      <input 
        type="text" 
        v-model="searchTerm" 
        placeholder="Search assignments..." 
        class="search-input"
      />
      <select v-model="selectedCourse" class="filter-select">
        <option value="">All Courses</option>
        <option v-for="course in courses" :key="course.id" :value="course.id">
          {{ course.title }}
        </option>
      </select>
    </div>

    <!-- Create/Edit Modal -->
    <div v-if="showCreateModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h3>{{ editingAssignment ? 'Edit Assignment' : 'Create Assignment' }}</h3>
        <form @submit.prevent="saveAssignment">
          <div class="form-group">
            <label>Title:</label>
            <input v-model="currentAssignment.title" required type="text" />
          </div>
          <div class="form-group">
            <label>Description:</label>
            <textarea v-model="currentAssignment.description" rows="4"></textarea>
          </div>
          <div class="form-group">
            <label>Course:</label>
            <select v-model="currentAssignment.courseId" required>
              <option value="">Select Course</option>
              <option v-for="course in courses" :key="course.id" :value="course.id">
                {{ course.title }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Lesson:</label>
            <select v-model="currentAssignment.lessonId">
              <option value="">Optional</option>
              <option v-for="lesson in lessons" :key="lesson.id" :value="lesson.id">
                {{ lesson.title }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Due Date:</label>
            <input v-model="currentAssignment.dueDate" type="datetime-local" />
          </div>
          <div class="form-group">
            <label>Max Score:</label>
            <input v-model.number="currentAssignment.maxScore" type="number" />
          </div>
          <div class="form-group">
            <label>Requirements:</label>
            <textarea v-model="currentAssignment.requirements" rows="4"></textarea>
          </div>
          <div class="modal-buttons">
            <button type="submit" class="btn btn-success">Save</button>
            <button type="button" @click="closeModal" class="btn btn-secondary">Cancel</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Assignments List -->
    <div class="assignments-list">
      <div v-if="filteredAssignments.length === 0" class="no-data">
        No assignments found
      </div>
      <div v-for="assignment in filteredAssignments" :key="assignment.id" class="assignment-card">
        <div class="assignment-header">
          <h3>{{ assignment.title }}</h3>
          <span class="status-badge" :class="assignment.status.toLowerCase()">
            {{ assignment.status }}
          </span>
        </div>
        <div class="assignment-meta">
          <p v-if="assignment.dueDate"><strong>Due:</strong> {{ formatDate(assignment.dueDate) }}</p>
          <p><strong>Max Score:</strong> {{ assignment.maxScore }} points</p>
        </div>
        <p class="description">{{ assignment.description }}</p>
        <div class="assignment-buttons">
          <button @click="viewAssignment(assignment)" class="btn btn-info">View Details</button>
          <button v-if="isAdmin" @click="editAssignment(assignment)" class="btn btn-warning">Edit</button>
          <button v-if="isAdmin" @click="deleteAssignment(assignment.id)" class="btn btn-danger">Delete</button>
          <button v-if="!isAdmin" @click="submitAssignment(assignment)" class="btn btn-success">Submit</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { assignmentService, courseService } from '../services/api.js';

export default {
  name: 'AssignmentList',
  data() {
    return {
      assignments: [],
      courses: [],
      lessons: [],
      showCreateModal: false,
      searchTerm: '',
      selectedCourse: '',
      currentAssignment: {
        title: '',
        description: '',
        courseId: '',
        lessonId: '',
        dueDate: '',
        maxScore: 100,
        requirements: '',
        status: 'ACTIVE'
      },
      editingAssignment: null,
      isAdmin: false
    };
  },
  computed: {
    filteredAssignments() {
      return this.assignments.filter(assignment => {
        const matchesSearch = assignment.title.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                             assignment.description?.toLowerCase().includes(this.searchTerm.toLowerCase());
        const matchesCourse = !this.selectedCourse || assignment.courseId === parseInt(this.selectedCourse);
        return matchesSearch && matchesCourse;
      });
    }
  },
  created() {
    this.isAdmin = localStorage.getItem('username') === 'axzil';
    this.fetchAssignments();
    this.fetchCourses();
  },
  methods: {
    async fetchAssignments() {
      try {
        const response = await assignmentService.getActiveAssignments();
        this.assignments = response.data;
      } catch (error) {
        console.error('Error fetching assignments:', error);
      }
    },
    async fetchCourses() {
      try {
        const response = await courseService.getCourses();
        this.courses = response.data;
      } catch (error) {
        console.error('Error fetching courses:', error);
      }
    },
    editAssignment(assignment) {
      this.editingAssignment = assignment;
      this.currentAssignment = { ...assignment };
      this.showCreateModal = true;
    },
    async saveAssignment() {
      try {
        if (this.editingAssignment) {
          await assignmentService.updateAssignment(this.editingAssignment.id, this.currentAssignment);
        } else {
          await assignmentService.createAssignment(this.currentAssignment);
        }
        this.fetchAssignments();
        this.closeModal();
      } catch (error) {
        console.error('Error saving assignment:', error);
        alert('Error saving assignment');
      }
    },
    async deleteAssignment(id) {
      if (confirm('Are you sure you want to delete this assignment?')) {
        try {
          await assignmentService.deleteAssignment(id);
          this.fetchAssignments();
        } catch (error) {
          console.error('Error deleting assignment:', error);
        }
      }
    },
    viewAssignment(assignment) {
      this.$router.push({ 
        name: 'AssignmentDetail', 
        params: { id: assignment.id } 
      });
    },
    submitAssignment(assignment) {
      this.$router.push({ 
        name: 'SubmitAssignment', 
        params: { id: assignment.id } 
      });
    },
    closeModal() {
      this.showCreateModal = false;
      this.editingAssignment = null;
      this.currentAssignment = {
        title: '',
        description: '',
        courseId: '',
        lessonId: '',
        dueDate: '',
        maxScore: 100,
        requirements: '',
        status: 'ACTIVE'
      };
    },
    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
  }
};
</script>

<style scoped>
.assignments-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.assignments-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  border-bottom: 2px solid #ddd;
  padding-bottom: 15px;
}

.assignments-header h2 {
  margin: 0;
  color: #333;
}

.filters {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.search-input,
.filter-select {
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.search-input {
  flex: 1;
  min-width: 200px;
}

.assignments-list {
  display: grid;
  gap: 20px;
}

.assignment-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  background: white;
  transition: box-shadow 0.3s;
}

.assignment-card:hover {
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.assignment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.assignment-header h3 {
  margin: 0;
  color: #333;
}

.status-badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  text-transform: uppercase;
}

.status-badge.active {
  background: #d4edda;
  color: #155724;
}

.status-badge.closed {
  background: #f8d7da;
  color: #721c24;
}

.status-badge.archived {
  background: #e2e3e5;
  color: #383d41;
}

.assignment-meta {
  display: flex;
  gap: 20px;
  margin: 10px 0;
  font-size: 14px;
  color: #666;
}

.assignment-meta p {
  margin: 0;
}

.description {
  color: #555;
  margin: 15px 0;
  line-height: 1.5;
}

.assignment-buttons {
  display: flex;
  gap: 10px;
  margin-top: 15px;
  flex-wrap: wrap;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: opacity 0.3s;
}

.btn:hover {
  opacity: 0.8;
}

.btn-primary {
  background: #007bff;
  color: white;
}

.btn-success {
  background: #28a745;
  color: white;
}

.btn-warning {
  background: #ffc107;
  color: black;
}

.btn-danger {
  background: #dc3545;
  color: white;
}

.btn-info {
  background: #17a2b8;
  color: white;
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 8px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-content h3 {
  margin-top: 0;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
  color: #333;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  font-family: inherit;
}

.form-group textarea {
  resize: vertical;
}

.modal-buttons {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
}

@media (max-width: 768px) {
  .assignments-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .filters {
    flex-direction: column;
  }

  .search-input,
  .filter-select {
    width: 100%;
  }

  .assignment-meta {
    flex-direction: column;
    gap: 5px;
  }

  .assignment-buttons {
    justify-content: flex-start;
  }

  .modal-content {
    width: 95%;
  }
}
</style>
