<template>
    <div class="password-reset-form container bg-light text-dark mt-2">
      <form @submit.prevent="submitForm" class="wrap-form p-3 mb-2 ">
        <h3 class="title">해당 이메일이 맞으신가요?</h3>
        <h3 class="text-center mb-10 mt-4 show-mail" style="color: #999999;">{{ showEmail }}</h3>
        <button type="submit" class="btn btn-primary text-center email-btn">제 이메일이 맞습니다.</button>
        <a class="back mt-3 d-block" @click="turnBack" >뒤로가기</a>
      </form>
    </div>
  </template>
    
  
    <script>
    import axios from 'axios';
    export default {
        props:['showEmail','userId'],
      data() {
        return {
        };
      },
      methods: {
        submitForm() {
            let loader = this.$loading.show();
          axios.post('http://localhost:8080/api/public/send-password',{userId : this.userId , email : this.showEmail})
          .then(()=>{
            alert("해당 메일로 임시 암호가 발급되었습니다.");
          }).catch(()=>{alert("오류가 발생했습니다.")})
          .finally(()=>{loader.hide()})
          this.turnBack();
        },
        turnBack(){
         this.$emit('switchLogin','login')
      },   
  
      },
    
    };
    </script>
    
    <style scoped>
   h1 {text-align: center;}
   .form-group{
    margin-bottom: 20px;
   }
   .title{
    margin-top: 50px;
   }

   .email-btn{
    margin: 0 auto;
    text-align: center;
    width: 400px;
    height: 50px;
    margin-bottom: 30px;
    margin-top: 40px;
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
  .show-mail{
    border: 1px solid rgba(224, 224, 224, 0.6);
    border-top: none;
    border-bottom: none;
    border-radius: 5px;
    height: 100px;
    line-height: 100px;
    width: 400px;
    margin: 0 auto;
  }
    </style>
    