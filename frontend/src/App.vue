<template>
  <div class="layout">
    <header>
      <h1>Online Education Platform</h1>
      <nav>
        <router-link to="/">Users</router-link>
        <router-link to="/add">Add User</router-link>
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
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const token = ref(localStorage.getItem('token'));

function logout() {
  localStorage.removeItem('token');
  token.value = null;
  router.push('/login');
}
</script>

<style scoped>
.layout { font-family: system-ui, Arial, sans-serif; max-width: 900px; margin: 0 auto; }
header { display: flex; flex-direction: column; gap: 8px; padding: 12px 0; }
nav { display: flex; gap: 12px; align-items: center; }
nav a { text-decoration: none; color: #1a4b84; font-weight: 600; }
nav a.router-link-active { border-bottom: 2px solid #1a4b84; }
button { background:#c62828; color:#fff; border:none; padding:6px 12px; cursor:pointer; border-radius:4px; }
main { margin-top: 20px; }
</style>
