<template>
  <div class="layout">
    <header>
      <div class="header-top">
        <h1>Online Education Platform</h1>
        <div v-if="token" class="user-info">
          <span>{{ username }}</span>
          <span :class="role === 'ADMIN' ? 'role-admin' : 'role-user'">{{ role }}</span>
        </div>
      </div>
      <nav>
        <router-link to="/">Users</router-link>
        <router-link to="/courses">Courses</router-link>
        <router-link to="/quizzes">Quizzes</router-link>
        <router-link to="/assignments">Assignments</router-link>
        <router-link to="/my-enrollments" v-if="token">My Courses</router-link>
        <router-link to="/quiz-management" v-if="isAdmin">Manage Quizzes</router-link>
        <router-link to="/assignment-management" v-if="isAdmin">Manage Assignments</router-link>
        <router-link to="/add" v-if="token">Add User</router-link>
        <router-link to="/login" v-if="!token">Login</router-link>
        <router-link to="/register" v-if="!token">Register</router-link>
        <button v-if="token" @click="logout">Logout</button>
      </nav>
    </header>
    <main>
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import authService from './services/auth';

const router = useRouter();
const token = ref(localStorage.getItem('token'));
const username = ref('');
const role = ref('');

const isAdmin = computed(() => role.value === 'ADMIN');

onMounted(() => {
  username.value = authService.getUsername();
  role.value = authService.getRole();
});

function logout() {
  authService.logout();
  token.value = null;
  username.value = '';
  role.value = '';
  router.push('/login');
}
</script>

<style scoped>
.layout { 
  font-family: system-ui, Arial, sans-serif; 
  max-width: 900px; 
  margin: 0 auto; 
}

header { 
  display: flex; 
  flex-direction: column; 
  gap: 8px; 
  padding: 12px 0; 
  border-bottom: 2px solid #e0e0e0;
}

.header-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-top h1 {
  margin: 0;
  font-size: 1.75rem;
}

.user-info {
  display: flex;
  gap: 1rem;
  align-items: center;
  font-size: 0.9rem;
}

.user-info span:first-child {
  font-weight: 500;
}

.role-admin {
  background-color: #ffcdd2;
  color: #d32f2f;
  padding: 0.25rem 0.75rem;
  border-radius: 3px;
  font-weight: bold;
  font-size: 0.8rem;
}

.role-user {
  background-color: #e3f2fd;
  color: #1976d2;
  padding: 0.25rem 0.75rem;
  border-radius: 3px;
  font-weight: bold;
  font-size: 0.8rem;
}

nav { 
  display: flex; 
  gap: 12px; 
  align-items: center; 
}

nav a { 
  text-decoration: none; 
  color: #1a4b84; 
  font-weight: 600; 
  padding: 0.5rem 0;
}

nav a.router-link-active { 
  border-bottom: 2px solid #1a4b84; 
}

nav a:hover {
  opacity: 0.8;
}

button { 
  background: #c62828; 
  color: #fff; 
  border: none; 
  padding: 0.5rem 1rem; 
  cursor: pointer; 
  border-radius: 4px;
  font-weight: 500;
}

button:hover {
  background: #b71c1c;
}

main { 
  margin-top: 20px; 
}
</style>
