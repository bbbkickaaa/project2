<template>
  <form id="LoginForm" @submit.prevent="onSignupSubmit" class="p-3 mb-2 bg-light text-dark">
    <div class="mb-3">
      <label for="signup-username" class="form-label">사용자명 :</label>
      <input id="signup-username" type="text" class="form-control" v-model.trim="signupForm.userid" required>
    </div>
    <div class="mb-3">
      <label for="signup-password" class="form-label">비밀번호 :</label>
      <input id="signup-password" type="password" class="form-control" v-model="signupForm.password" required>
    </div>
    <div class="mb-3">
      <label for="signup-email" class="form-label">이메일 :</label>
      <input id="signup-email" type="email" class="form-control" v-model="signupForm.email" required>
    </div>
    <button id="submit" type="submit" class="btn btn-secondary" :disabled="!isPossible">회원가입</button>
    <div class ="labels">
      <label class="showText" v-if="!isUserIdValid && signupForm.userid"> {{ showTextId }} </label>
      <label class="showText" v-if="!isPasswordValid && signupForm.password"> {{ showTextPw }} </label>
      <label class="showText" v-if="!isEmailValid && signupForm.email"> {{ showTextEmail }} </label>
  </div>
    <div class="mt-3">
      <a  href="http://localhost:8080/oauth2/authorization/google?redirect_uri=http://localhost:3000/oauth2/redirect">
        <img style="width: 200px; height: 50px;" src="../assets/img/google/web_dark_sq_SU@3x.png" id="loginGoogle" alt="" class="img-fluid">
      </a>
    </div>
  </form>
</template>

  
  <script>
import axios from 'axios';
  export default {
    data() {
      return {
        showTextId : "아이디가 너무 짧습니다.",
        showTextPw : "비밀번호가 너무 짧습니다.",
        showTextEmail : "이메일 형식에 어긋납니다.",
        isUserIdValid: false,
        isPasswordValid: false,
        isEmailValid: false,

        signupForm: {
          userid: '',
          password: '',
          email: ''
        }
      };
    },
    computed: {
    isPossible() {
      return this.isUserIdValid && this.isPasswordValid && this.isEmailValid;
    }
  },
    methods: {
      onSignupSubmit() {
        axios.post('http://localhost:8080/api/public/join', this.signupForm)
          .then(response => {
            alert(response.data); // 상태 코드를 알림으로 표시
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
    },
    updateIsPossible() {
      const { userid, password, email } = this.signupForm;
      this.isUserIdValid = userid.length > 6;
      this.isPasswordValid = password.length > 8;
      this.isEmailValid = email.length > 10 && email.includes('@') && email.includes('.');
    }
  },
 watch: {
  'signupForm.userid': function() {
    this.updateIsPossible();
  },
  'signupForm.password': function() {
    this.updateIsPossible();
  },
  'signupForm.email': function() {
    this.updateIsPossible();
  }
}
}
  </script>
  <style>
  .showText{
    margin-top: 5px;
    margin-right: 10px;
  }
  .labels{height: 50px; width: auto; margin-bottom: 20px;}
  </style>