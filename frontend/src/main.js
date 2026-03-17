import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { createRouter, createWebHistory } from 'vue-router'
import App from './App.vue'
import { routes } from './router'

const router = createRouter({ history: createWebHistory(), routes })
createApp(App).use(ElementPlus).use(router).mount('#app')
