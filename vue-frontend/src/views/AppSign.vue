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
  