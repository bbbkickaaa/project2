import { createApp } from 'vue';
import { createRouter, createWebHistory } from 'vue-router';
import App from '@/App.vue';
//

import AppIntro from '@/views/intro/AppIntro.vue';
import MainBoard from '@/views/main/mainRouter/MainBoard.vue';
import ReDirect from '@/views/intro/ReDirect.vue';
import NewPost from '@/views/main/mainRouter/NewPost.vue';
import BoardDetails from '@/views/main/mainRouter/Details/BoardDetails.vue';
import BoardAlter from  '@/views/main/mainRouter/BoardAlter.vue';
import MainLayout from '@/components/layout/MainLayout.vue';
import { checkPostOwner } from '@/routerGuard';
import {validateCategoryRoute} from '@/routerGuard';
import Identity from '@/views/main/mainRouter/IdentityContent.vue';
import { validateYourRoleRoute } from '@/routerGuard';
import ReportBoard from '@/views/main/mainRouter/ReportBoard.vue';
import NoticePost from '@/views/main/mainRouter/NoticePost.vue';
import NoticeDetail from '@/views/main/mainRouter/Details/NoticeDetails.vue';
import NoticeAlter from '@/views/main/mainRouter/NoticeAlter.vue';
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
        { path:'favorite', component:MainBoard},
        { path: 'report-list', component:ReportBoard , beforeEnter:validateYourRoleRoute},
        { path: 'identity', component: Identity },
        { path: 'post', component: NewPost }, 
        { path: ':category1/:category2?/:category3?',props:true, component: MainBoard, beforeEnter:validateCategoryRoute},
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
        {path: 'notice/write', component:NoticePost, beforeEnter:validateYourRoleRoute},
        {path: 'notice/details/:noticeId', component:NoticeDetail},
        {path: 'notice/details/:noticeId/edit', component:NoticeAlter,beforeEnter:validateYourRoleRoute}
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
