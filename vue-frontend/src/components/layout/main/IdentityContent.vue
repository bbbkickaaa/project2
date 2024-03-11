<template>
    <div class="user-identity">
      <div><span class="material-symbols-outlined turn-back" @click="$router.push('/main')">undo</span></div>
      <div class="form-title">
        <h2>나의 정보</h2>
        <p>본 페이지에서 회원정보 수정이 가능합니다.</p>
      </div>
      <div class="user-image">
        <a>
          <img :src="userData.picture" style="border-radius: 50%; width:150px; height: 150px; border: 5px solid darkolivegreen;" alt="profile">
          <span class="material-symbols-outlined camera">photo_camera</span>
        </a>
      </div>
      <form @submit.prevent="submitPost">
        <div class="form-group">
          <label for="userid">아이디</label>
          <input type="text" class="form-input" id="userid" v-model="form.id" disabled>
        </div>
  
        <div class="form-group">
          <label for="nickname">닉네임</label>
          <input type="text" placeholder="새 닉네임 입력" class="form-input" minlength="3" maxlength="8" id="nickname" v-model="form.nickname">
        </div>
  
        <div class="form-group">
          <label for="password">새 비밀번호</label>
          <input type="password" class="form-input" minlength="8" placeholder="새 비밀번호 입력" id="password" v-model="form.password">
        </div>

        <div class="form-group">
          <label for="password-check">비밀번호 확인</label>
          <input type="password" class="form-input" minlength="8" placeholder="비밀번호 확인" id="password-check" v-model="passwordCheck">
        </div>
  
        <div class="form-group">
          <label for="email">이메일</label>
          <input type="email" :disabled="true" class="form-input" id="email" v-model="userData.email">
        </div>
        <div  class="delete-button">  
          <button type="submit" class="btn btn-primary form-button " style="color: white;"><i class="material-symbols-outlined">check</i> 변경 사항 저장</button>
          <button type="submit" class="btn btn-secondary form-button" @click ="$router.push('/main')">뒤로가기</button>

          <div class="delete-user" >
            <a @click="ShowModal = true" @close="closeModal">회원탈퇴</a>
        </div>
        </div>

      </form>
    </div>

    <modal-component :show="ShowModal" @close="ShowModal = false" class="">
                <div class="modal-inner">
                <h4 style="text-align: center; padding-top: 50px;">정말 탈퇴하시겠습니까?</h4>
                    <div class="button-outer">
                        <button class="btn btn-secondary modal-button" @click="deleteAccount">네</button>
                        <button class="btn btn-secondary modal-button" @click="ShowModal = false">취소</button>
                    </div>
                </div>
    </modal-component>
  </template>
  
  <script>
  import ModalComponent from '../ModalComponent.vue';
  import axios from 'axios';
  import store from '@/store';
  export default {
    components: {
         ModalComponent
        },
    data() {
      return {
        form:{
            id : '',
            nickname:'',
            password : ''
        },
        passwordCheck : '',
        ShowModal: false,
        id : null ,
        userData: {
            id : '' ,
            userid: '',
            userLevel:{level : null, points: null},
            createdDate: '',
            nickname: '',
            postCount:'',
            commentCount:'',
            picture: '',
      }
      }
    },
    methods: {
      deleteAccount() {
      this.$axios.post('/api/member/delete-user',this.id)
      .then(response=>{alert(response.data);
      axios.post('http://localhost:8080/api/public/logout', null, {withCredentials: true})
      sessionStorage.clear(); 
      store.commit('clearState');
      this.$router.push('/intro');
      })
    },
    submitPost(){
        if(this.form.password === this.passwordCheck){
        this.$axios.post('/api/member/alter-user',this.form)
        .then(response=>{alert(response.data); this.$router.push('/main')})
    }else{ alert("패스워드가 일치하지 않습니다.")}},
},
    watch: {
        ShowModal(newValue) {
            if (newValue) {
            this.showModal = false;
            }
        }
},
    mounted(){
    this.$axios.get('/api/member/get-user')
          .then(response => {
            this.userData = response.data;
            this.form.id = response.data.userid;
          })
          .catch(error => {
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert("토큰이 만료되었습니다.");
            }
        }),
        this.id = sessionStorage.getItem('userIdx');
}}
  </script>
  
  <style scoped>
  .user-image{
    position: relative;
    width: 150px;
    margin: 0 auto;
    margin-bottom: 20px;
  }
  .user-image:hover{
    cursor: pointer;
  }
  .camera{
    color: slategrey;
    font-size: 50px;
    position: absolute;
    right: 15px;
    bottom: 10px;
  }
  .user-identity {
    border: 1px solid rgba(108, 117, 125, 0.2);
    margin: 80px auto;
    width: 1000px;
    padding: 20px;
    box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    height: 1000px;
  }
  .form-title{
    width: 900px;
    padding-left: 30px;
    margin-left: 70px;
    padding-top: 30px;
    margin-bottom: 30px;
  }
  .form-title h2 {
    margin-bottom: 30px;
  }
  .form-group{
    padding: 15px 60px;
    margin-left: 100px;
  }
  .form-group label {
    width: 120px;
    height: 60px;
    line-height: 60px;
    margin-right: 50px;
    border-right : 1px solid rgba(108, 117, 125, 1);
    display: inline-block;
  }
  .modal-inner{
        box-sizing: border-box;
        padding: 30px;
        border: 1px solid rgba(108, 117, 125, 0.2);
        height: 400px;
        margin-top: 30px;
    }
    .modal-button{
        margin: 0 auto;
        width: 100px;
        margin-right: 20px;
    }
    .button-outer{
        box-sizing: border-box;
        border-top: 1px dashed rgba(108, 117, 125, 0.2);
        margin-left: 100px;
        margin: 0 auto;
        padding-top: 30px;
        margin-top: 180px;
        height: 150px;
        padding-left: 130px;
    }
    .form-button{
        margin-left: 30px;
        height: 50px;
    }
    .form-button:first-child{
      width: 160px;
    }
    
    .form-button:last-child {
      width: 120px;
    }
    .form-input{
      width: 400px;
      height: 60px;
      padding: 20px;
    }

    .material-symbols-outlined{
      vertical-align: middle;
    }
    .delete-user {
      text-decoration: underline;
      margin-right: 20px;
      color: #999999;
      text-align: right;
    }
    .delete-user a:hover{
      cursor: pointer;
    }
    .delete-button{
      margin-left: 20px;
      margin-top: 60px;
    }
    .turn-back{
        margin-left:10px; 
        font-size: 50px; 
        color: #999999;
    }
    .turn-back:hover {
        color: black;
        cursor: pointer;
    }

  </style>
  