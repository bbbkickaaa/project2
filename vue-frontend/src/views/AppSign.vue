<template>
    <form @submit.prevent="onSignupSubmit">
      <div>
        <label for="signup-username">사용자명:</label>
        <input id="signup-username" type="text" v-model="signupForm.userid" required>
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
import axios from 'axios';
  export default {
    data() {
      return {
        signupForm: {
          userid: '',
          password: '',
          email: ''
        }
      };
    },
    methods: {
      onSignupSubmit() {
        axios.post('http://localhost:8082/api/public/join', this.signupForm)
          .then(response => {
            alert(response.status.body); // 상태 코드를 알림으로 표시
          })
          .catch(error => {
            if (error.response) {
              alert(`${error.response.status.body}`);
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert(`${error.message}`);
            }
        });
    }
  }
}
  </script>
  <style>
    #loginGoogle{
      width: 300px;
      height: 80px;
    }
  </style>