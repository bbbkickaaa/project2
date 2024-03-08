<template>
  <form id="LoginForm" @submit.prevent="onSignupSubmit" class="p-3 mb-2 bg-light text-dark">
    <div class="mb-3 login-form">
      <label for="signup-username">유저 아이디</label>
      <input id="signup-username" type="text" class="form-control" placeholder="아이디를 입력해 주세요." v-model.trim="signupForm.userid" required>
      <label for="signup-password">비밀번호</label>
      <input id="signup-password" type="password" class="form-control" placeholder="비밀번호를 입력해 주세요." v-model="signupForm.password" required>
      <label for="signup-email" >이메일</label>
      <div class = "email-form">
        <input id="signup-email" placeholder="이메일을 입력해 주세요." type="email" class="form-control" style="width: 300px; float: left;" :disabled="isSendAuthenticateMail" v-model="signupForm.email" required>
        <button :disabled="!isEmailValid || getResponseMailSend" placeholder="인증번호를 입력해 주세요." @click="AuthenticateMail" class="btn btn-secondary send-mail">인증</button>
      </div>

      <label v-if="isSendAuthenticateMail" for="authenticate-email" >인증번호</label>
      <div v-if="isSendAuthenticateMail" class = "email-form">
        <input minlength="5" id="authenticate-email"  class="form-control" style="width: 300px; float: left;" v-model="AuthenticationNumber" required>
        <button type="button" :disabled="AuthenticationNumber.length<5 || !getResponseMailSend" @click="CheckMailCorrect($event)" class="btn btn-primary send-mail"> 확인</button>
      </div>
    </div>
    <div class="create-wrap">
      <button id="submit" type="submit" class="btn btn-primary create-user" :disabled="!isPossible">회원가입</button>
    </div>




    <div class ="labels">
      <label class="show-text" v-if="!isUserIdValid && signupForm.userid"> {{ showTextId }} </label>
      <label class="show-text" v-if="!isPasswordValid && signupForm.password"> {{ showTextPw }} </label>
      <label class="show-text" v-if="!isEmailValid && signupForm.email"> {{ showTextEmail }} </label>
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
        wasAuthenticated : false,
        isSendAuthenticateMail : false,
        getResponseMailSend : false,

        isAuthenticatedMail : false,
        AuthenticationNumber : '',
        signupForm: {
          userid: '',
          password: '',
          email: '',
          isAuthenticated : false,
        }
      };
    },
    computed: {
    isPossible() {
      return this.isUserIdValid && this.isPasswordValid && this.isEmailValid && this.isSendAuthenticateMail && this.wasAuthenticated;
    }
  },
    methods: {
      onSignupSubmit() {
        axios.post('http://localhost:8080/api/public/join', this.signupForm)
          .then(response => {
            alert(response.data); // 상태 코드를 알림으로 표시
            this.$emit('close-modal');
          })
          .catch(error=> {
            if (error.response) {
              alert(error.response.data);
              this.$router.push("/intro");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert(error.response.data);
            }
        });
    },
    updateIsPossible() {
      const { userid, password, email } = this.signupForm;
      this.isUserIdValid = userid.length > 6;
      this.isPasswordValid = password.length > 8;
      this.isEmailValid = email.length > 10 && email.includes('@') && email.includes('.');
    },
    AuthenticateMail(){
      var submitMail = false;
      this.isSendAuthenticateMail = true;
      if(!submitMail){ 
        axios.post('http://localhost:8080/api/public/mail-send', { email : this.signupForm.email,
          headers: {'Content-Type': 'application/json'}} )
          .then((response)=>{
        submitMail = true;
        this.getResponseMailSend = true;
        console.log(response)}
        )
    }
  },
    CheckMailCorrect(event){
      event.stopPropagation()
      if(this.AuthenticationNumber.length<5){
        alert("인증번호가 짧습니다.")
      }
      else{
      axios.post('http://localhost:8080/api/public/mail-auth-check', 
      {email: this.signupForm.email , authNum:this.AuthenticationNumber,
    headers: {
        'Content-Type': 'application/json'
    }} ).then(()=>{
      alert("일치합니다.");
      this.wasAuthenticated = true;
      }).catch(()=>{
        alert("일치하지 않습니다.")
      })
    }},
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
  <style scoped>
  .show-text{
    margin-top: 5px;
    margin-right: 10px;
  }
  .labels{height: 50px; width: auto; margin-bottom: 20px;}

  .img-fluid{ margin-right: 20px; margin-top: 20px;}

  .login-form{
    width: 500px;

  }
  .form-control{
    width: 400px;
    height: 60px;
    margin: 10px auto;

  }
  .send-mail{
    position: absolute;
    right: 0;
    top: 10px;
    width: 80px;
    height: 60px;
  }
  .email-form{
    width: 400px;
    height: 80px;
    margin: 0 auto;
    position: relative;
  }
  .create-user{
    width: 400px;
    height: 60px;
    margin: 0 auto;
  }
  .create-wrap{
    width: 410px;
    margin: 0 auto;
  }
  .login-form label{
    padding-left: 30px;
  }
  </style>