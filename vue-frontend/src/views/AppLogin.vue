<template>
    <form @submit.prevent="onLoginSubmit">
      <div>
        <label for="login-username">사용자명:</label>
        <input id="login-username" type="text" v-model="loginForm.username" required>
      </div>
      <div>
        <label for="login-password">비밀번호:</label>
        <input id="login-password" type="password" v-model="loginForm.password" required>
      </div>
      <button type="submit">로그인</button>
    </form>
  </template>
  
  <script>
  export default {
    data() {
      return {
        loginForm: {
          username: '',
          password: ''
        }
      };
    },
    methods: {
      onLoginSubmit() {
        // 로그인 처리 로직
        alert(`로그인 시도: ${this.loginForm.username}`);
      }
    }
  };
  </script>
  