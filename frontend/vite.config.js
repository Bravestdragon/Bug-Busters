import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';

// Add alias for '@' to resolve to /src so imports like '@/services/api' work
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // Your backend URL
        changeOrigin: true,
        secure: false
      }
    }
  }
});