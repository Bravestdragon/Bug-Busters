<template>
  <section>
    <h2>{{ isEdit ? 'Edit User' : 'Add User' }}</h2>
    <form @submit.prevent="submit">
      <label>Username <input v-model="form.username" required /></label>
      <label>Password <input v-model="form.password" type="password" :required="!isEdit" /></label>
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
    const { data } = await api.get('/api/users/' + route.params.id);
    form.value = { ...data, password: '' };
  }
});

async function submit() {
  try {
    if (isEdit.value) {
      await api.put('/api/users/' + route.params.id, form.value);
    } else {
      await api.post('/api/users', form.value);
    }
    router.push('/');
  } catch (e) {
    alert('Save failed (maybe unauthorized).');
  }
}
</script>
