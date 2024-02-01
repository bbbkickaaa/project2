<template>
    <form @submit.prevent="onSignupSubmit">
      <div>
        <label for="signup-username">사용자명:</label>
        <input id="signup-username" type="text" v-model="signupForm.username" required>
      </div>
      <div>
        <label for="signup-password">비밀번호:</label>
        <input id="signup-password" type="password" v-model="signupForm.password" required>
      </div>
      <div>
        <label for="signup-email">이메일:</label>
        <input id="signup-email" type="email" v-model="signupForm.email" required>
      </div>
      <button type="submit">회원가입</button>
      <div>
        <a href="http://localhost:8082/oauth2/authorization/google?redirect_uri=http://localhost:3000/oauth2/redirect">
          <img src="../assets/img/google/web_dark_sq_SU@3x.png" id ="loginGoogle" alt="">
        </a>
      </div>
    </form>
  </template>
  
  <script>
  export default {
    data() {
      return {
        signupForm: {
          username: '',
          password: '',
          email: ''
        }
      };
    },
    methods: {
      onSignupSubmit() {
        // 회원가입 처리 로직
        alert(`회원가입 시도: ${this.signupForm.username}`);
      }
    }
    
  };
  </script>
  <style>
    #loginGoogle{
      width: 300px;
      height: 80px;
    }
  </style>