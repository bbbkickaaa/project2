<template>
    <form @submit.prevent="onLoginSubmit">
      <div>
        <label for="login-username">사용자명:</label>
        <input id="login-username" type="text" v-model="loginForm.userid" required>
      </div>
      <div>
        <label for="login-password">비밀번호:</label>
        <input id="login-password" type="password" v-model="loginForm.password" required>
      </div>
      <button type="submit">로그인</button>
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
        axios.post('http://localhost:8080/api/public/login', this.loginForm)
    .then(response => {
      const authHeader = response.headers['authorization'] || response.headers['Authorization'];
      if (authHeader) {
        const token = authHeader.split(' ')[1]; // "Bearer TOKEN" 형식을 가정
        sessionStorage.setItem('accessToken', token);
        this.$router.push({ path: '/main' });
      } else {
        this.$router.push({path:'/intro'})
        throw new Error('Authorization 토큰이 없습니다.');
      }
    })
    .catch(error => {
      // 오류 처리
      console.error('로그인 실패:', error);
      alert("로그인에 실패했습니다.")
    });
  }
}
  }
  </script>
  