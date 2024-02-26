import { createApp } from 'vue';
import { createRouter, createWebHistory } from 'vue-router';
import App from './App.vue';
//

import AppIntro from './views/AppIntro.vue';
//
import MainLayout from './components/layout/MainLayout.vue';
import MainBoard from './components/layout/main/MainBoard.vue';
import AppAbout from './views/AppAbout.vue';
import ReDirect from "./views/ReDirect.vue";
import NewPost from "./components/layout/main/NewPost.vue";
import BoardDetails from "./components/layout/main/BoardDetails.vue"
import BoardAlter from "./components/layout/main/BoardAlter.vue"
import { checkPostOwner } from './routerGuard';

const router = createRouter({
  history: createWebHistory(),
  routes : [
    {
      path: '/intro',
      component: AppIntro
    },
    {
      path: '/',
      redirect: '/intro'
    },
    {
      path: '/main',
      component: MainLayout,
      props: true,
      children: [
        { path: 'post', component: NewPost }, 
        { 
          path: 'detail/:id', 
          component: BoardDetails},
        {path: 'detail/:id/edit', component: BoardAlter, name: 'BoardAlter' ,beforeEnter: checkPostOwner},
        { path: '', component: MainBoard }, 
        { path: 'about', component: AppAbout } 
      ]
    },
    { path: '/oauth2/authorization/google' },
    { path: '/oauth2/redirect', component: ReDirect },
    { path: '/another-path' }
  ]
  
    // 나머지 모든 정의되지 않은 경로를 /main으로 리다이렉트
    //{ path: '/:pathMatch(.*)*', redirect: '/main' }
});


const app = createApp(App);
app.use(router);
app.mount('#app');

router.beforeEach((to, from, next) => {
  const getToken = () => sessionStorage.getItem('accessToken');
  const isAuthenticated = !!getToken();

  if (to.path.startsWith('/main')) {
      if (isAuthenticated) {
        next();
      } else {
        next('/intro');
      }
    } else if (to.path == '/intro' && isAuthenticated) {
      next('/main');
    } else {
      next();
    }
  });


export default router;
