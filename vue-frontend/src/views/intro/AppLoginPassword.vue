<template>
  <div class="password-reset-form container bg-light text-dark mt-2">
    <form @submit.prevent="submitForm" class="wrap-form p-3 mb-2 ">
      <h3 class="text-center title">비밀번호를 분실 하셨나요?</h3>
      <div class="form-group">
        <input type="text" class="form-control" placeholder="회원 아이디를 입력해 주세요." minlength="6" id="userId" v-model="userId" required>
      </div>
      <button type="submit" class="btn btn-success text-center password-btn">비밀번호를 찾고 싶어요.</button>
      <a class="back mt-3 d-block" @click="turnBack" >뒤로가기</a>
    </form>
  </div>
</template>
  

  <script>
  import axios from 'axios';
  export default {
    data() {
      return {
        userId: '',
      };
    },
    methods: {
      submitForm() {
        axios.post('http://localhost:8080/api/public/forgot-password',this.userId)
        .then((response)=>{
          var email = response;
          this.$emit('presentEmail',{email:email.data , userId:this.userId})
        }).catch((error)=>{alert(error.response.data)})
      },
      turnBack(){
       this.$emit('switchLogin','login')
    },   

    },
  
  };
  </script>
  
  <style scoped>
 h1 {text-align: center;}
 #userId{
  width: 400px;
  height: 60px;
  border-radius: 5px;
  margin: 0 auto;
  text-align: center;
 }
 .title{
  margin-top: 50px;
  margin-bottom: 60px;}
 .form-group{
  margin-bottom: 40px;
  margin-top: 30px;
 }
 .password-btn{
  margin: 0 auto;
  text-align: center;
  width: 400px;
  height: 50px;
  margin-bottom: 30px;
 }
 .wrap-form {
  height: 400px;
  text-align: center;
 }
 .back{
  text-align: right;
  width: 400px;
  margin: 0 auto;
  color: #999999;
 }
.back:hover {
  cursor: pointer;
}
  </style>
  