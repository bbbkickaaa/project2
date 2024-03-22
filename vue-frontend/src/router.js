import { createApp } from 'vue';
import { createRouter, createWebHistory } from 'vue-router';
import App from './App.vue';
//

import AppIntro from './views/AppIntro.vue';
import MainBoard from './components/layout/main/MainBoard.vue';
import ReDirect from "./views/ReDirect.vue";
import NewPost from "./components/layout/main/NewPost.vue";
import BoardDetails from "./components/layout/main/BoardDetails.vue"
import BoardAlter from "./components/layout/main/BoardAlter.vue"
import MainLayout from './components/layout/MainLayout.vue';

import { checkPostOwner } from './routerGuard';
import {validateCategoryRoute} from './routerGuard';
import Identity from './components/layout/main/IdentityContent.vue'

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
        { path: '', component: MainBoard },
        { path: ':category1/:category2?/:category3?',props:true, component: MainBoard, beforeEnter:validateCategoryRoute},
        { path:'favorite', component:MainBoard},
        { path: 'identity', component: Identity },
        { path: 'post', component: NewPost }, 
        { 
          path: ':category1/:category2/:category3/detail/:id', 
          component: BoardDetails,
          name : 'boardDetail',
          beforeEnter:validateCategoryRoute
        },
        { 
          path: ':category1/:category2/:category3/detail/:id/edit', 
          component: BoardAlter, 
          name: 'BoardAlter', 
          beforeEnter: checkPostOwner
        },
      ]
    },
    
    { path: '/oauth2/authorization/google' },
    { path: '/oauth2/redirect', component: ReDirect },
    { path: '/another-path' },
    

    { path: '/:pathMatch(.*)*', redirect: '/main' }
  ]
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
