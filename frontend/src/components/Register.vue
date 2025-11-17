<template>
  <section>
    <h2>Register</h2>
    <form @submit.prevent="register">
      <label>Username <input v-model="username" required /></label>
      <label>Password <input type="password" v-model="password" required minlength="3" /></label>
      <label>Email <input type="email" v-model="email" /></label>
      <button type="submit">Create Account</button>
      <p v-if="message" :class="status">{{ message }}</p>
    </form>
  </section>
</template>

<script setup>
import { ref } from 'vue';
import { userService } from '../services/api'; // Use userService

const username = ref('');
const password = ref('');
const email = ref('');
const message = ref('');
const status = ref('');

async function register() {
  message.value = '';
  status.value = '';
  try {
    await userService.register({ 
      username: username.value, 
      password: password.value, 
      email: email.value 
    });
    message.value = 'Registration successful. You can log in now.';
    status.value = 'ok';
    username.value = password.value = email.value = '';
  } catch (e) {
    message.value = e.response?.status === 409 ? 'Username already exists' : 'Registration failed';
    status.value = 'err';
    console.error('Registration error:', e.response?.data || e.message);
  }
}
</script>

<style scoped>
.ok { color: green; margin-top: 8px; }
.err { color: #c62828; margin-top: 8px; }
</style>