import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue';
import Vuex from 'vuex';
import axios from 'axios';

// Import Bootstrap and BootstrapVue CSS files (order is important)
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

const app = createApp(App);
app.use(Vuex);
app.use(BootstrapVue);
app.use(IconsPlugin);
app.use(router);

// axiosConfig.js
const axiosInstance = axios.create({
  baseURL: 'https://localhost:8080'
});

axiosInstance.interceptors.request.use(config => {
  const token = sessionStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => {
  return Promise.reject(error);
});

app.config.globalProperties.$axios = axiosInstance;
app.mount('#app');