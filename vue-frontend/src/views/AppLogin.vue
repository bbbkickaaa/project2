<template>
  <form @submit.prevent="onLoginSubmit" class="p-3 mb-2 bg-light text-dark">
    <div class="mb-3">
      <label for="login-username" class="form-label">사용자명:</label>
      <input id="login-username" type="text" class="form-control" v-model.trim="loginForm.userid" required>
    </div>
    <div class="mb-3">
      <label for="login-password" class="form-label">비밀번호:</label>
      <input id="login-password" type="password" class="form-control" v-model="loginForm.password" required>
    </div>
    <button type="submit" class="btn btn-secondary">로그인</button>
    <div class="oauth-form">
      <a  href="http://localhost:8080/oauth2/authorization/google?redirect_uri=http://localhost:3000/oauth2/redirect">
        <img style="width: 200px; height: 50px;" src="../assets/img/oauth/web_dark_sq_SU@3x.png" id="login-google" alt="" class="img-fluid">
      </a>
      <a  href="http://localhost:8080/oauth2/authorization/naver?redirect_uri=http://localhost:3000/oauth2/redirect">
        <img style="width: 200px; height: 50px;" src="../assets/img/oauth/btnW_complete.png" id="login-naver" alt="" class="img-fluid">
      </a>
      <a  href="http://localhost:8080/oauth2/authorization/kakao?redirect_uri=http://localhost:3000/oauth2/redirect">
        <img style="width: 200px; height: 50px;" src="../assets/img/oauth/kakao_login_medium_narrow.png" id="login-kakao" alt="" class="img-fluid">
      </a>
    </div>
  </form>
</template>
  
  <script>
  import axios from 'axios';
  export default {
    data() {
      return {
        loginForm: {
          userid: '',
          password: ''
        }
      };
    },
    methods: {
      onLoginSubmit() {
        axios.post('http://localhost:8080/api/public/login', this.loginForm,{withCredentials: true})
    .then(response => {
      const authHeader = response.headers['authorization'] || response.headers['Authorization'];
      if (authHeader) {
        const token = authHeader.split(' ')[1];
        sessionStorage.setItem('accessToken', token);

        this.$router.push({ path: '/main' });
        alert("로그인 성공")
      } else {
        this.$router.push({path:'/intro'})
        throw new Error('Authorization 토큰이 없습니다.');
      }
    })
    .catch(error => {
      alert("로그인에 실패했습니다.")
      console.log(error);
    });
  }
}
  }
  </script>

  <style>

.img-fluid{ margin-right: 20px; margin-top: 20px;}

.oauth-form a{width: 200px; height: 50px;}

  </style>
  