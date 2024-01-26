import { createApp } from 'vue';
import { createRouter, createWebHistory } from 'vue-router';
import App from './App.vue';
import AppHome from './views/AppHome.vue';
import AppAbout from './views/AppAbout.vue';

const app = createApp(App);

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: AppHome
    },
    {
      path: '/about',
      component: AppAbout
    }
  ]
});

app.use(router);
app.mount('#app');
