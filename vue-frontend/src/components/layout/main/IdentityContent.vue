<template>
    <div class="user-identity">
      <form @submit.prevent="submitPost">
        <div class="form-group">
          <label for="userid">아이디</label>
          <input type="text" class="form-control" id="userid" v-model="form.id" disabled>
        </div>
  
        <div class="form-group">
          <label for="nickname">닉네임</label>
          <input type="text" class="form-control" minlength="3" maxlength="8" id="nickname" v-model="form.nickname">
        </div>
  
        <div class="form-group">
          <label for="password">패스워드</label>
          <input type="password" class="form-control" minlength="8" placeholder="새 비밀번호 입력." id="password" v-model="form.password">
          <label v-show="form.password">구글 로그인일 경우 패스워드를 변경하시면 해당 아이디 및 비밀번호로 로그인이 가능합니다.</label>
        </div>
  
        <div class="form-group">
          <label for="email">이메일</label>
          <input type="email" :disabled="true" class="form-control" id="email" v-model="userData.email">
        </div>
  
        <button type="submit" class="btn btn-primary form-button">저장</button>
        <button type="submit" class="btn btn-secondary form-button" @click ="$router.push('/main')">뒤로가기</button>
        <div>
            <button type="button" class="btn btn-secondary form-button" @click="ShowModal = true" @close="closeModal">회원탈퇴</button>
        </div>
      </form>
    </div>

    <modal-component :show="ShowModal" @close="ShowModal = false" class="">
                <div class="modalInner">
                <h4 style="text-align: center; padding-top: 50px;">정말 탈퇴하시겠습니까?</h4>
                    <div class="ButtonOuter">
                        <button class="btn btn-secondary modalButton" @click="deleteAccount">네</button>
                        <button class="btn btn-secondary modalButton" @click="ShowModal = false">취소</button>
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
            password : ''
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
        this.$axios.post('/api/member/alter-user',this.form)
        .then(response=>{alert(response.data); this.$router.push('/main')})
    },
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
  
  <style>
  .user-identity {
    border: 1px solid rgba(108, 117, 125, 0.5);
    margin: 80px auto;
    width: 1000px;
    padding: 20px;
  }
  .form-group{
    padding: 30px;
    margin-bottom: 30px;
  }
  .modalInner{
        box-sizing: border-box;
        padding: 30px;
        border: 1px solid rgba(108, 117, 125, 0.2);
        height: 400px;
        margin-top: 30px;
    }
    .modalButton{
        margin: 0 auto;
        width: 100px;
        margin-right: 20px;
    }
    .ButtonOuter{
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
        margin-bottom: 100px;
    }
  </style>
  