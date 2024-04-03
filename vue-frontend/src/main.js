import { createApp } from 'vue';
import App from '@/App.vue';
import router from './router';
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue';
import Vuex from 'vuex';
import axios from 'axios';
import store from './store';
import {LoadingPlugin} from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/css/index.css';

// Import Bootstrap and BootstrapVue CSS files (order is important)
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

const app = createApp(App);
app.use(Vuex);
app.use(BootstrapVue);
app.use(IconsPlugin);
app.use(router);
app.use(LoadingPlugin, {
  color: '#000',
  loader: 'spinner',
  backgroundColor: 'rgba(0, 0, 0, 0.6)', // 어두운 배경
  opacity: 0.5,
  zIndex: 9999, // 높은 z-index
});
// axiosConfig.js
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080'
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
let reloadCount = 0;
let isRefreshing = false;
let refreshPromise = null;
// 로그아웃 함수 정의
const logout = () => {
  axios.delete('http://localhost:8080/api/public/logout', null, { withCredentials: true })
    .then(() => {
      sessionStorage.clear(); 
      store.commit('clearState');
      router.push({ path:'/intro' });
    });
};

axiosInstance.interceptors.response.use(response => {
  return response;
}, async function (error) {
  if (!sessionStorage.getItem('accessToken') || (error.response && error.response.status === 401)) {
    if (!isRefreshing) {
      isRefreshing = true;
      refreshPromise = axiosInstance.post('api/public/refresh', null, {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`
        },
        withCredentials: true
      }).then(response => {

        const authHeader = response.headers['authorization'] || response.headers['Authorization'];
        const newToken = authHeader.split(' ')[1];
        sessionStorage.setItem('accessToken', newToken);
        const originalRequest = error.config;
        originalRequest.headers.Authorization = `Bearer ${newToken}`;
        if(reloadCount < 6){
          window.location.reload();
          reloadCount++;
        }
        return axiosInstance(originalRequest);
      }).catch(refreshError => {
        logout();
        return Promise.reject(refreshError);
      }).finally(() => {
        isRefreshing = false;
      });
    }
    return refreshPromise.catch(refreshError => {
      logout();
      return Promise.reject(refreshError);
    });
  }
  return Promise.reject(error);
});

app.config.globalProperties.$axios = axiosInstance;
app.mount('#app');