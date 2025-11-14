import { createRouter, createWebHistory } from 'vue-router';
import UserList from './components/UserList.vue';
import UserForm from './components/UserForm.vue';
import UserDetail from './components/UserDetail.vue';
import Login from './components/Login.vue';
import Register from './components/Register.vue';

const routes = [
  { path: '/', component: UserList },
  { path: '/add', component: UserForm },
  { path: '/edit/:id', component: UserForm, props: true },
  { path: '/user/:id', component: UserDetail, props: true },
  { path: '/login', component: Login },
  { path: '/register', component: Register }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
