import { createApp } from 'vue';
import { createRouter, createWebHistory } from 'vue-router';
import App from './App.vue';
//

import AppIntro from './views/AppIntro.vue';
//
import MainLayout from './components/layout/MainLayout.vue';
import AppHome from './views/AppHome.vue';
import AppAbout from './views/AppAbout.vue';
import Redirect from "./views/ReDirect.vue";
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/intro',
      component: AppIntro
    },
    {
      path: '/main',
      component: MainLayout,
      children: [
        { path: '', component: AppHome },
        { path: 'about', component: AppAbout }
      ]
    },
    {path: '/oauth2/authorization/google'},
    {path: '/oauth2/redirect', component: Redirect},
    { path: '/another-path' },
    // 나머지 모든 정의되지 않은 경로를 /main으로 리다이렉트
    //{ path: '/:pathMatch(.*)*', redirect: '/main' }
  ]
});


// 그 다음 Vue 앱 인스턴스를 생성하고, 라우터를 사용합니다.
const app = createApp(App);
app.use(router);
app.mount('#app');

router.beforeEach((to, from, next) => {
  const isAuthenticated = true; // 토큰 유효성 검사 로직

  if (!isAuthenticated && to.path !== '/intro') {
    next('/intro'); // 인증되지 않았다면 /intro로 리다이렉트
  } else {
    next(); // 인증되었다면 요청된 경로로 진행
  }
});

export default router;
