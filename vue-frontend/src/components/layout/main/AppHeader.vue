<template>
    <header>
      <div class="container">
          <div class="user-info py-3">
              <h1>반갑습니다. {{ userData.nickname }}님.</h1>
              <p class="level">회원 레벨: <span class="badge bg-secondary">{{userData.userLevel.level}}</span></p>
              <p class="registration-date">가입일: <span>{{userData.createdDate}}</span></p>
              <p class="posts">글 갯수: <span>123</span></p>
              <p class="comments">댓글 갯수: <span>456</span></p>
          </div>
          <button class="logout" @click="logout">로그아웃</button>
      </div>
  </header>
</template>
<script>
import axios from 'axios';
import router from '@/router';
import store from '@/store';
export default {
data(){
    return{
      userData: {
        userid: '',
        userLevel:{level : null, points: null},
        createdDate: '',
        nickname: ''
      }
    }
},
mounted(){
  this.$axios.get('/api/member/getUser')
          .then(response => {
            console.log(response.data)
            this.userData = response.data;
          })
          .catch(error => {
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert(`${error.message}`);
            }
        });
},

methods:{
    logout(){
      axios.post('http://localhost:8080/api/public/logout', null, {withCredentials: true})
      sessionStorage.clear(); 
      store.commit('clearState');
      router.push({path:'/intro'})
    }
}
}
</script>