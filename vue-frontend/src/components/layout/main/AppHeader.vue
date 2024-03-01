<template>
    <header>
      <div class="container">
          <div class="user-info py-3">
              <h4>반갑습니다. {{ userData.nickname }}님.</h4>
              <p class="level">회원 레벨: <span class="badge bg-secondary">{{userData.userLevel.level}}</span></p>
              <p class="registration-date">가입일: <span>{{userData.createdDate}}</span></p>
              <p class="posts">글 갯수: {{userData.postCount}}</p>
              <p class="comments">댓글 갯수: {{ userData.commentCount }}</p>
              <button class="btn btn-secondary edit" @click="AlterIdentity" >정보수정</button>
              <button class="btn btn-secondary logout" @click="logout">로그아웃</button>
          </div>
          
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
        id : '' ,
        userid: '',
        userLevel:{level : null, points: null},
        createdDate: '',
        nickname: '',
        postCount:'',
        commentCount:''
      }
    }
},
mounted(){
  this.$axios.get('/api/member/get-user')
          .then(response => {
            this.userData = response.data;
            this.getUserPostCount(this.userData.id)
            sessionStorage.setItem('userIdx',this.userData.id)
          })
          .catch(error => {
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert("토큰이 만료되었습니다.");
            }
        })
},

methods:{
    logout(){
      axios.post('http://localhost:8080/api/public/logout', null, {withCredentials: true})
      sessionStorage.clear(); 
      store.commit('clearState');
      router.push({path:'/intro'})
    },
    getUserPostCount(id) {
          this.$axios.get('/api/board/get-post-count', {
            params: {
              id: id
            }
          })
          .then(response => {
            this.userData.commentCount = response.data.commentCount
            this.userData.postCount = response.data.postCount
          })
          .catch(error => {
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert("토큰이 만료되었습니다.");
            }
        });
    },
    AlterIdentity(){
      this.$emit('Alter-identity');
    }
}
}
</script>

<style scoped>
.user-info{
  width: 1000px;
  height: 100px;
  border: 1px solid rgba(108, 117, 125, 0.5);
  margin: 80px auto;
  padding: 20px;
  position: relative;
}

.user-info p { 
  display: inline-block;
  padding-right: 20px;
  }

.user-info .logout { 
  position: absolute;
  right: 30px;
  bottom: 20px;
  }
.user-info .edit { 
  position: absolute;
  right: 130px;
  bottom: 20px;
  }

</style>