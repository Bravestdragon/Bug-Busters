import { createApp } from 'vue'
import App from './App.vue'
// Import the router index so the routes in `src/router/index.js` are used
import router from './router/index.js'
import './style.css'

const app = createApp(App)
app.use(router)
app.mount('#app')