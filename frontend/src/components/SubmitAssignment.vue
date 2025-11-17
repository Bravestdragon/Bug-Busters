<template>
  <div class="submit-assignment-container">
    <div class="back-button">
      <button @click="$router.back()" class="btn btn-secondary">← Back</button>
    </div>

    <div v-if="assignment" class="submission-form">
      <div class="assignment-info">
        <h2>{{ assignment.title }}</h2>
        <div class="info-grid">
          <div class="info-item">
            <label>Due Date:</label>
            <p>{{ formatDate(assignment.dueDate) }}</p>
          </div>
          <div class="info-item">
            <label>Max Score:</label>
            <p>{{ assignment.maxScore }} points</p>
          </div>
          <div class="info-item">
            <label>Status:</label>
            <p :class="assignment.status.toLowerCase()">{{ assignment.status }}</p>
          </div>
        </div>
        <div class="description-section">
          <h3>Assignment Description</h3>
          <p>{{ assignment.description }}</p>
        </div>
        <div v-if="assignment.requirements" class="requirements-section">
          <h3>Requirements</h3>
          <p>{{ assignment.requirements }}</p>
        </div>
      </div>

      <div class="submission-section">
        <h3>Submit Your Work</h3>
        <form @submit.prevent="submitWork">
          <div class="form-group">
            <label>Submission Text:</label>
            <textarea 
              v-model="submission.submissionText" 
              placeholder="Enter your response or notes here..."
              rows="8"
              class="large-textarea"
            ></textarea>
          </div>

          <div class="form-group">
            <label>Upload File:</label>
            <div class="file-upload">
              <input 
                type="file" 
                @change="handleFileUpload" 
                class="file-input"
                id="file-input"
              />
              <label for="file-input" class="file-label">
                <span v-if="!selectedFile">Choose File or Drag Here</span>
                <span v-else>{{ selectedFile.name }}</span>
              </label>
            </div>
            <p class="help-text">Supported formats: PDF, DOC, DOCX, XLS, XLSX, ZIP (Max 10MB)</p>
          </div>

          <div v-if="existingSubmission" class="existing-submission">
            <h4>Previous Submission</h4>
            <p><strong>Submitted:</strong> {{ formatDate(existingSubmission.submittedAt) }}</p>
            <p v-if="existingSubmission.score !== null">
              <strong>Score:</strong> {{ existingSubmission.score }} / {{ assignment.maxScore }}
            </p>
            <p v-if="existingSubmission.feedback">
              <strong>Feedback:</strong> {{ existingSubmission.feedback }}
            </p>
          </div>

          <div class="form-buttons">
            <button type="submit" class="btn btn-primary">
              {{ existingSubmission ? 'Resubmit' : 'Submit Assignment' }}
            </button>
            <button type="button" @click="clearForm" class="btn btn-secondary">Clear</button>
          </div>
        </form>
      </div>
    </div>

    <div v-else class="loading">
      Loading assignment...
    </div>
  </div>
</template>

<script>
import { assignmentService, assignmentSubmissionService } from '../services/api.js';

export default {
  name: 'SubmitAssignment',
  data() {
    return {
      assignment: null,
      existingSubmission: null,
      submission: {
        submissionText: '',
        submissionFile: ''
      },
      selectedFile: null,
      enrollmentId: null,
      userId: null
    };
  },
  created() {
    this.assignmentId = this.$route.params.id;
    this.enrollmentId = this.$route.params.enrollmentId || 1;
    this.userId = localStorage.getItem('userId') || 1;
    this.fetchAssignment();
    this.checkExistingSubmission();
  },
  methods: {
    async fetchAssignment() {
      try {
        const response = await assignmentService.getAssignmentById(this.assignmentId);
        this.assignment = response.data;
      } catch (error) {
        console.error('Error fetching assignment:', error);
        alert('Error loading assignment');
      }
    },
    async checkExistingSubmission() {
      try {
        const response = await assignmentSubmissionService.getStudentSubmission(
          this.assignmentId, 
          this.userId
        );
        this.existingSubmission = response.data;
        if (this.existingSubmission) {
          this.submission.submissionText = this.existingSubmission.submissionText || '';
        }
      } catch (error) {
        this.existingSubmission = null;
      }
    },
    handleFileUpload(event) {
      this.selectedFile = event.target.files[0];
      if (this.selectedFile && this.selectedFile.size > 10 * 1024 * 1024) {
        alert('File size exceeds 10MB limit');
        this.selectedFile = null;
        event.target.value = '';
      }
    },
    async submitWork() {
      if (!this.submission.submissionText && !this.selectedFile) {
        alert('Please provide either text or file submission');
        return;
      }

      try {
        const submissionData = {
          assignmentId: this.assignmentId,
          enrollmentId: this.enrollmentId,
          userId: this.userId,
          submissionText: this.submission.submissionText,
          submissionFile: this.selectedFile ? this.selectedFile.name : this.submission.submissionFile
        };

        if (this.existingSubmission) {
          await assignmentSubmissionService.updateSubmission(
            this.existingSubmission.id,
            submissionData
          );
        } else {
          await assignmentSubmissionService.submitAssignment(submissionData);
        }

        alert('Assignment submitted successfully!');
        this.checkExistingSubmission();
        this.clearForm();
      } catch (error) {
        console.error('Error submitting assignment:', error);
        alert('Error submitting assignment');
      }
    },
    clearForm() {
      this.submission.submissionText = this.existingSubmission?.submissionText || '';
      this.selectedFile = null;
      document.getElementById('file-input').value = '';
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
.submit-assignment-container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.back-button {
  margin-bottom: 20px;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: opacity 0.3s;
}

.btn:hover {
  opacity: 0.8;
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-primary {
  background: #007bff;
  color: white;
}

.submission-form {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.assignment-info {
  padding: 30px;
  background: #f8f9fa;
  border-bottom: 1px solid #ddd;
}

.assignment-info h2 {
  margin-top: 0;
  color: #333;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin: 20px 0;
  padding: 20px;
  background: white;
  border-radius: 4px;
}

.info-item label {
  display: block;
  font-weight: bold;
  color: #666;
  margin-bottom: 5px;
  font-size: 14px;
}

.info-item p {
  margin: 0;
  color: #333;
  font-size: 16px;
}

.description-section,
.requirements-section {
  margin-top: 20px;
}

.description-section h3,
.requirements-section h3 {
  color: #333;
  margin-bottom: 10px;
}

.submission-section {
  padding: 30px;
}

.submission-section h3 {
  color: #333;
  margin-top: 0;
}

.form-group {
  margin-bottom: 25px;
}

.form-group label {
  display: block;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.large-textarea {
  width: 100%;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-family: monospace;
  font-size: 14px;
  resize: vertical;
}

.file-upload {
  position: relative;
}

.file-input {
  display: none;
}

.file-label {
  display: block;
  padding: 20px;
  border: 2px dashed #007bff;
  border-radius: 4px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  color: #007bff;
  font-weight: bold;
  background: #f0f7ff;
}

.file-label:hover {
  background: #e7f3ff;
  border-color: #0056b3;
}

.help-text {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}

.existing-submission {
  background: #e8f5e9;
  padding: 20px;
  border-radius: 4px;
  border-left: 4px solid #4caf50;
  margin: 20px 0;
}

.existing-submission h4 {
  color: #2e7d32;
  margin-top: 0;
}

.existing-submission p {
  margin: 8px 0;
  color: #333;
}

.form-buttons {
  display: flex;
  gap: 10px;
  justify-content: flex-start;
  margin-top: 30px;
}

.loading {
  padding: 40px;
  text-align: center;
  color: #999;
}

@media (max-width: 768px) {
  .submit-assignment-container {
    padding: 10px;
  }

  .assignment-info,
  .submission-section {
    padding: 20px;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .form-buttons {
    flex-direction: column;
  }

  .btn {
    width: 100%;
  }
}
</style>
