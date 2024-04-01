<template>
  <form @submit.prevent="onLoginSubmit" class="p-3 mb-2 bg-light text-dark">
    <div class="oauth-form">
      <a  href="http://localhost:8080/oauth2/authorization/google?redirect_uri=http://localhost:3000/oauth2/redirect">
      <div class="img-form" style="background-color: white;" >
        <div>
           <img src="@/assets/img/oauth/web_dark_sq_SU@3x.png" id="login-google" alt="" class="img-fluid">
           <label class="img-label">구글 계정 로그인</label>
          </div>
        </div>
      </a>
      <a  href="http://localhost:8080/oauth2/authorization/naver?redirect_uri=http://localhost:3000/oauth2/redirect">
      <div class="img-form" style="background-color: #03C75A; color: #f2f2f2;">
        <div>
            <img src="@/assets/img/oauth/btnW_complete.png"  id="login-naver" alt="" class="img-fluid">
            <label class="img-label">네이버 계정 로그인</label>
          </div>
        </div>
      </a>
      <a href="http://localhost:8080/oauth2/authorization/kakao?redirect_uri=http://localhost:3000/oauth2/redirect">
        <div class="img-form" style="background-color: #FEE500;">
          <div>
            <img src="@/assets/img/oauth/kakao_login_medium_narrow.png" id="login-kakao" alt="" class="img-fluid">
            <label class="img-label">카카오 계정 로그인</label>
          </div>
        </div>
      </a>
    </div>
    <div class="login-wrap">
      <div class="mb-2 login-form">
        <input id="login-username" placeholder="유저아이디를 입력해 주세요." type="text" class="form-control" v-model.trim="loginForm.userid" required>
      </div>
      <div class="mb-2 login-form">
        <input id="login-password" placeholder="비밀번호를 입력해 주세요." type="password" class="form-control" v-model="loginForm.password" required>
      </div>
      <button type="submit" class="btn btn-success submit-login">이메일 계정 로그인</button>
      <a class="forgot-password" @click="forgotPassword">비밀번호를 잊으셨나요?</a>
    </div>
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
        axios.post('http://localhost:8080/api/public/login', this.loginForm,{withCredentials: true})
      .then(response => {
        const authHeader = response.headers['authorization'] || response.headers['Authorization'];
        if (authHeader) {
          const token = authHeader.split(' ')[1];
          sessionStorage.setItem('accessToken', token);

          this.$router.push({ path: '/main' });
          alert("로그인 성공")
        } else {
          this.$router.push({path:'/intro'})
          throw new Error('Authorization 토큰이 없습니다.');
        }
      })
      .catch((e) => {
        console.log(e.response);
        alert(e.response.data);
      });
  },
  forgotPassword(){
    this.$emit('switchComponent', 'password');
  },
  
}
  }
  </script>

  <style scoped>

.img-fluid{ margin-right: 20px; margin-top: 14px; width: 30px; height: 30px;}
.img-form {
  width: 400px; height: 60px; border-radius: 5px; border: 1px solid #ccc; 
  padding-left: 30px;
  margin:  10px auto;
  text-decoration: none;
  color: #252525;

}
.img-form div {
  position: relative;
}
.img-label {
  position: absolute;
  bottom: 3px;
}
.img-label:hover {
  cursor: pointer;
}
.form-control{
  width: 400px;
  height: 60px;

}
.login-form{
  width: 400px;
  height: 60px;
  margin: 10px auto;
  margin-bottom: 20px;
}
.login-wrap{
  width: 508px;
  padding-top: 15px;
  margin-top: 15px;
  border-top: 1px solid rgba(204,204,204,0.2);
  text-align: center;
 
}
.submit-login{
  width: 400px;
  height: 60px;
  display: block;
  margin: 20px auto;
}
.forgot-password{
  text-align: right; color: #999999; display: block;
}
.forgot-password:hover{
  cursor: pointer;
}
  </style>
  