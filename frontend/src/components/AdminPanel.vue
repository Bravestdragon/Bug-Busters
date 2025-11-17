<template>
  <section v-if="isAdmin" class="admin-panel">
    <h3>Admin Panel</h3>
    <div class="admin-info">
      <p><strong>Logged in as:</strong> {{ username }}</p>
      <p><strong>Role:</strong> <span class="role-badge admin">{{ role }}</span></p>
      <button @click="logout" class="logout-btn">Logout</button>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import authService from '../services/auth';

const router = useRouter();
const isAdmin = ref(false);
const username = ref('');
const role = ref('');

onMounted(() => {
  isAdmin.value = authService.isAdmin();
  username.value = authService.getUsername();
  role.value = authService.getRole();
});

function logout() {
  authService.logout();
  router.push('/login');
}
</script>

<style scoped>
.admin-panel {
  background-color: #fff3e0;
  border: 2px solid #ff9800;
  border-radius: 4px;
  padding: 1rem;
  margin: 1rem 0;
}

.admin-panel h3 {
  color: #e65100;
  margin-top: 0;
}

.admin-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.admin-info p {
  margin: 0;
  font-size: 0.95rem;
}

.role-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 3px;
  font-weight: bold;
  font-size: 0.85rem;
}

.role-badge.admin {
  background-color: #ffcdd2;
  color: #d32f2f;
}

.logout-btn {
  background-color: #d32f2f;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.95rem;
  width: fit-content;
  margin-top: 0.5rem;
}

.logout-btn:hover {
  background-color: #b71c1c;
}
</style>
