<template>
  <section>
    <h2>User List</h2>
    <div v-if="loading">Loading...</div>
    <div v-else>
      <table v-if="users.length" class="users">
        <thead><tr><th>ID</th><th>Username</th><th>Email</th><th>Actions</th></tr></thead>
        <tbody>
          <tr v-for="u in users" :key="u.id">
            <td>{{ u.id }}</td>
            <td><router-link :to="'/user/' + u.id">{{ u.username }}</router-link></td>
            <td>{{ u.email }}</td>
            <td>
              <router-link :to="'/edit/' + u.id">Edit</router-link>
              <button @click="remove(u.id)">Delete</button>
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

const users = ref([]);
const loading = ref(true);

async function load() {
  loading.value = true;
  try {
    const { data } = await api.get('/api/users');
    users.value = data;
  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
}

async function remove(id) {
  if (!confirm('Delete user #' + id + '?')) return;
  try {
    await api.delete('/api/users/' + id);
    users.value = users.value.filter(u => u.id !== id);
  } catch (e) {
    alert('Delete failed (maybe unauthorized).');
  }
}

onMounted(load);
</script>
<style scoped>
.users { border-collapse: collapse; width:100%; }
.users th, .users td { border:1px solid #ddd; padding:6px 8px; }
.users th { background:#f5f5f5; text-align:left; }
button { margin-left:6px; }
</style>
