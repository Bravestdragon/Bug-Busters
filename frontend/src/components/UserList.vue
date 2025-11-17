<template>
  <section>
    <h2>User List</h2>
    <div v-if="loading">Loading...</div>
    <div v-else>
      <table v-if="users.length" class="users">
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Role</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="u in users" :key="u.id">
            <td>{{ u.id }}</td>
            <td>
              <router-link :to="'/user/' + u.id">{{ u.username }}</router-link>
            </td>
            <td>{{ u.email }}</td>
            <td>
              <span :class="u.role === 'ADMIN' ? 'role-admin' : 'role-user'">
                {{ u.role }}
              </span>
            </td>
            <td>
              <router-link v-if="isAdmin || (currentUsername && currentUsername.toLowerCase() === u.username.toLowerCase())" :to="'/edit/' + u.id">Edit</router-link>
              <button v-if="isAdmin" @click="remove(u.id)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-else>No users found.</p>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import api from '../services/api';
import authService from '../services/auth';

const users = ref([]);
const loading = ref(true);
const isAdmin = authService.isAdmin();
const currentUsername = authService.getUsername();

async function load() {
  loading.value = true;
  try {
    const { data } = await api.get('/users');
    users.value = data;
  } catch (error) {
    console.error('Failed to load users:', error);
    alert('Failed to load users');
  } finally {
    loading.value = false;
  }
}

async function remove(id) {
  if (!confirm(`Delete user #${id}?`)) return;
  
  try {
    await api.delete(`/users/${id}`);
    users.value = users.value.filter(u => u.id !== id);
  } catch (error) {
    console.error('Delete failed:', error);
    alert(`Delete failed: ${error.response?.data || error.message}`);
  }
}

onMounted(load);
</script>

<style scoped>
.users {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.users th, .users td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.users th {
  background-color: #f5f5f5;
  font-weight: bold;
}

.users tbody tr:hover {
  background-color: #f9f9f9;
}

.role-admin {
  color: #d32f2f;
  font-weight: bold;
  background-color: #ffebee;
  padding: 0.25rem 0.5rem;
  border-radius: 3px;
}

.role-user {
  color: #1976d2;
  background-color: #e3f2fd;
  padding: 0.25rem 0.5rem;
  border-radius: 3px;
}

a, button {
  margin-right: 0.5rem;
  padding: 0.25rem 0.5rem;
  border: 1px solid #ccc;
  background-color: #fff;
  cursor: pointer;
  border-radius: 3px;
  text-decoration: none;
  color: #1976d2;
}

button {
  background-color: #ffebee;
  border-color: #d32f2f;
  color: #d32f2f;
}

button:hover {
  background-color: #ffcdd2;
}
</style>