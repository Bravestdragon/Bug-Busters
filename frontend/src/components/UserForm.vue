<template>
  <section>
    <h2>{{ isEdit ? 'Edit User' : 'Add User' }}</h2>
    <form @submit.prevent="submit">
      <label>Username <input v-model="form.username" required /></label>
      <label>Password 
        <input v-model="form.password" type="password" 
               :required="!isEdit" :placeholder="isEdit ? 'Leave blank to keep current' : ''" />
      </label>
      <label>Email <input v-model="form.email" type="email" /></label>
        <label v-if="isAdmin">Role
          <select v-model="form.role">
            <option value="USER">Regular User</option>
            <option value="ADMIN">Administrator</option>
          </select>
        </label>
      <button type="submit">{{ isEdit ? 'Update' : 'Create' }}</button>
      <router-link to="/">Cancel</router-link>
    </form>
  </section>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import api from '../services/api';
import authService from '../services/auth';

const route = useRoute();
const router = useRouter();
const form = ref({ username: '', password: '', email: '', role: 'USER' });

const isEdit = computed(() => !!route.params.id);
const isAdmin = authService.isAdmin();

onMounted(async () => {
  if (isEdit.value) {
    try {
      const { data } = await api.get(`/users/${route.params.id}`);
      form.value = { ...data, password: '' }; // Don't include current password
    } catch (error) {
      console.error('Failed to load user:', error);
      alert('Failed to load user data');
    }
  }
});

async function submit() {
  try {
    const payload = { ...form.value };
    
    // Remove password if empty during edit
    if (isEdit.value && !payload.password) {
      delete payload.password;
    }

    if (isEdit.value) {
      await api.put(`/users/${route.params.id}`, payload);
    } else {
      await api.post('/users', payload);
    }
    
    router.push('/');
  } catch (error) {
    console.error('Save failed:', error);
    alert(`Save failed: ${error.response?.data || error.message}`);
  }
}
</script>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  max-width: 400px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

input, select {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
}

button, a {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  text-decoration: none;
  text-align: center;
}

button {
  background-color: #1976d2;
  color: white;
  margin-right: 0.5rem;
}

button:hover {
  background-color: #1565c0;
}

a {
  background-color: #e0e0e0;
  color: #333;
  display: inline-block;
  width: fit-content;
}

a:hover {
  background-color: #d0d0d0;
}
</style>