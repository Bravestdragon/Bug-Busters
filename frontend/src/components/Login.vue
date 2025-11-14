<template>
  <section>
    <h2>Login</h2>
    <form @submit.prevent="login">
      <label>Username <input v-model="username" required /></label>
      <label>Password <input type="password" v-model="password" required /></label>
      <button type="submit">Login</button>
      <p v-if="error" class="err">{{ error }}</p>
    </form>
  </section>
</template>
<script setup>
import { ref } from 'vue';
import api from '../services/api';
import { useRouter } from 'vue-router';

const router = useRouter();
const username = ref('');
const password = ref('');
const error = ref('');

async function login() {
  error.value = '';
  try {
    const { data } = await api.post('/api/users/login', { username: username.value, password: password.value });
    localStorage.setItem('token', data.token);
    router.push('/');
  } catch (e) {
    error.value = 'Invalid credentials';
  }
}
</script>
<style scoped>.err{color:#c62828;margin-top:8px;}</style>
