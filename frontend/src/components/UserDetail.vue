<template>
  <section v-if="user">
    <h2>User #{{ user.id }}</h2>
    <p><strong>Username:</strong> {{ user.username }}</p>
    <p><strong>Email:</strong> {{ user.email }}</p>
    <router-link to="/">Back</router-link>
  </section>
  <p v-else>Loading...</p>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import api from '../services/api';

const route = useRoute();
const user = ref(null);

async function load() {
  try {
    const { data } = await api.get('/api/users/' + route.params.id);
    user.value = data;
  } catch (e) {
    console.error(e);
  }
}

onMounted(load);
</script>
