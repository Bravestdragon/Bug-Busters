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
      <button type="submit">{{ isEdit ? 'Update' : 'Create' }}</button>
      <router-link to="/">Cancel</router-link>
    </form>
  </section>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import api from '../services/api';

const route = useRoute();
const router = useRouter();
const form = ref({ username: '', password: '', email: '' });

const isEdit = computed(() => !!route.params.id);

onMounted(async () => {
  if (isEdit.value) {
    try {
      const { data } = await api.get(`/api/users/${route.params.id}`);
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
      await api.put(`/api/users/${route.params.id}`, payload);
    } else {
      await api.post('/api/users', payload);
    }
    
    router.push('/');
  } catch (error) {
    console.error('Save failed:', error);
    alert(`Save failed: ${error.response?.data || error.message}`);
  }
}
</script>