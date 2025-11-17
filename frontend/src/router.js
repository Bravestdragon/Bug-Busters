import { createRouter, createWebHistory } from 'vue-router';
import UserList from './components/UserList.vue';
import UserForm from './components/UserForm.vue';
import UserDetail from './components/UserDetail.vue';
import Login from './components/Login.vue';
import Register from './components/Register.vue';
import CourseList from './components/CourseList.vue';
import CourseForm from './components/CourseForm.vue';
import CourseDetail from './components/CourseDetail.vue';
import MyEnrollments from './components/MyEnrollments.vue';

const routes = [
  { path: '/', component: UserList },
  { path: '/add', component: UserForm },
  { path: '/edit/:id', component: UserForm, props: true },
  { path: '/user/:id', component: UserDetail, props: true },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/courses', component: CourseList },
  { path: '/courses/create', component: CourseForm },
  { path: '/courses/:id', component: CourseDetail, props: true },
  { path: '/courses/:id/edit', component: CourseForm, props: true },
  { path: '/my-courses', component: MyEnrollments }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
