<template>
    <form id="LoginForm" @submit.prevent="onSignupSubmit">
      <div class="forms">
        <label for="signup-username" class= "forms_label">UserId :</label>
        <input id="signup-username" type="text" v-model="signupForm.userid" required>
      </div>
      <div class="forms">
        <label for="signup-password" class= "forms_label">PassWord :</label>
        <input id="signup-password" type="password" v-model="signupForm.password" required>
      </div> 
      <div class="forms">
        <label for="signup-email" class= "forms_label">Email :</label>
        <input id="signup-email" type="email" v-model="signupForm.email" required>
      </div>
      <button id="submit" type="submit" :disabled="!isPossible" >회원가입</button>
     <label class="showText" v-if="!isUserIdValid && signupForm.userid"> {{ showTextId }} </label>
     <label class="showText" v-if="!isPasswordValid && signupForm.password"> {{ showTextPw }} </label>
     <label class="showText" v-if="!isEmailValid && signupForm.email"> {{ showTextEmail }} </label>
     <div>
        <a href="http://localhost:8080/oauth2/authorization/google?redirect_uri=http://localhost:3000/oauth2/redirect">
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
  #LoginForm{
    position: relative;
  }
    #loginGoogle{
      width: 300px;
      height: 80px;
      position: absolute;
      top: 350px;
    }
    .forms{
      padding: 10px;
      width: 400px;
      height: 50px;
    }
    #submit{
      margin-top: 50px;
      display: block;
    }
    .forms .forms_label{
      padding-right: 20px;
      width: 110px;
      
    }
    .showText{
      display: block;
    }
  </style>