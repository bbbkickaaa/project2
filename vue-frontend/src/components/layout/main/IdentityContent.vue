<template>
    <div class="user-identity">
      <form>
        <div class="form-group">
          <label for="userid">User ID</label>
          <input type="text" class="form-control" id="userid" v-model="user.userid" disabled>
        </div>
  
        <div class="form-group">
          <label for="nickname">Nickname</label>
          <input type="text" class="form-control" id="nickname" v-model="user.nickname">
        </div>
  
        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" class="form-control" id="password" v-model="user.password">
        </div>
  
        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" class="form-control" id="email" v-model="user.email">
        </div>
  
        <button type="submit" class="btn btn-primary">저장</button>
        <button type="submit" class="btn btn-secondary" @click ="$router.push('/main')">뒤로가기</button>
        <button type="button" class="btn btn-secondary" @click="deleteAccount">회원탈퇴</button>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        user: {
          userid: 'user123',
          nickname: 'Nickname',
          password: 'password123',
          email: 'user@example.com'
        },
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
    methods: {
      deleteAccount() {
      }
    },
    mounted(){
    this.$axios.get('/api/member/getUser')
          .then(response => {
            this.userData = response.data;
            console.log(this.userData);
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
}}
  </script>
  
  <style>
  .user-identity {
    border: 1px solid rgba(108, 117, 125, 0.5);
    margin: 80px auto;
    width: 1000px;
    padding: 20px;
  }
  </style>
  