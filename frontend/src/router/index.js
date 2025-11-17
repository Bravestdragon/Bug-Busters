import { createRouter, createWebHistory } from 'vue-router'
import UserList from '../components/UserList.vue'
import UserForm from '../components/UserForm.vue'
import UserDetail from '../components/UserDetail.vue'
import Login from '../components/Login.vue'
import Register from '../components/Register.vue'
import CourseList from '../components/CourseList.vue'
import CourseDetail from '../components/CourseDetail.vue'
import Enrollment from '../components/Enrollment.vue'
import MyEnrollments from '../components/MyEnrollments.vue'
import LessonView from '../components/LessonView.vue'
import QuizList from '../components/QuizList.vue'
import QuizTake from '../components/QuizTake.vue'
import QuizManagement from '../components/QuizManagement.vue'
import ProgressDisplay from '../components/ProgressDisplay.vue'
import AssignmentList from '../components/AssignmentList.vue'
import SubmitAssignment from '../components/SubmitAssignment.vue'
import AssignmentManagement from '../components/AssignmentManagement.vue'

const routes = [
  { path: '/', component: UserList },
  { path: '/add', component: UserForm },
  { path: '/edit/:id', component: UserForm, props: true },
  { path: '/user/:id', component: UserDetail, props: true },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/courses', component: CourseList },
  { path: '/course/:id', component: CourseDetail, props: true },
  { path: '/enroll/:courseId', component: Enrollment, props: true },
  { path: '/my-enrollments', component: MyEnrollments },
  { path: '/lesson/:lessonId', name: 'LessonView', component: LessonView, props: true },
  { path: '/quizzes', component: QuizList },
  { path: '/quiz/:quizId', name: 'QuizTake', component: QuizTake, props: true },
  { path: '/quiz-management', name: 'QuizManagement', component: QuizManagement },
  { path: '/progress/:courseId/:enrollmentId', name: 'ProgressDisplay', component: ProgressDisplay, props: true },
  { path: '/assignments', name: 'AssignmentList', component: AssignmentList },
  { path: '/assignment/:id/submit', name: 'SubmitAssignment', component: SubmitAssignment, props: true },
  { path: '/assignment-management', name: 'AssignmentManagement', component: AssignmentManagement }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router