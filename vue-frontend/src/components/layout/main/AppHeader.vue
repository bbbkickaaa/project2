<template>
    <header>
      <div class="container">
          <div class="user-info">
              <h4><img :src="userData.picture" style="width: 40px; height: 40px; margin-right: 10px; border-radius: 50%; border: 3px solid darkolivegreen;"> <span>    반갑습니다. {{ userData.nickname }}님. </span>
            <span class="material-symbols-outlined symbols star" v-if="!IsFavorite" @click="toFavorite">star</span>
            <span class="material-symbols-outlined symbols star fill-star" v-if="IsFavorite" @click="toMain">star</span>
              </h4>
              <p class="level">회원 레벨: <span class="badge bg-secondary">{{userData.userLevel.level}}</span></p>
              <p class="registration-date">가입일: <span>{{userData.createdDate}}</span></p>
              <p class="posts">글 갯수: {{userData.postCount}}</p>
              <p class="comments">댓글 갯수: {{ userData.commentCount }}</p>
              <div class="header-button">
                 <div> 
                    <a><span class="material-symbols-outlined">notifications</span></a>
                    <span class="material-symbols-outlined plus" style="position: absolute; border-radius: 50%; background-color:#FFF455; color: brown;">add</span>
                 </div>
                 <div> 
                    <a @click="showGroupModal"><span class="material-symbols-outlined">groups</span></a>
                </div>
                <button class="btn btn-success edit" @click="AlterIdentity" >정보수정</button>
                <button class="btn btn-secondary logout" @click="logout">로그아웃</button>
              </div>
          </div>   
      </div>
      <modal-component :show="ShowModal" @close="ShowModal = false"  class="">
        <app-friend @show-message="setMessageId" v-if="!friendWithMessageId"></app-friend>
        <user-message @switch-modal="backModal" v-if="friendWithMessageId"></user-message>
      </modal-component>
  </header>
</template>
<script>
import axios from 'axios';
import router from '@/router';
import store from '@/store';
import ModalComponent from '../ModalComponent.vue';
import AppFriend from '../main/AppFriend.vue'
import UserMessage from './UserMessage.vue';
export default {
  components :{
    ModalComponent,AppFriend
    ,UserMessage
  },
data(){
    return{
      IsFavorite:null,
      friendWithMessageId:null,
      ShowModal : false,
      userData: {
        id : '' ,
        userid: '',
        userLevel:{level : null, points: null},
        createdDate: '',
        nickname: '',
        postCount:'',
        commentCount:'',
        picture:'',
      }
    }
},
mounted(){
  this.$axios.get('/api/member/get-user')
          .then(response => {
            this.userData = response.data;
            this.getUserPostCount(this.userData.id)
            sessionStorage.setItem('userIdx',this.userData.id)
          })
          .catch(error => {
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert("토큰이 만료되었습니다.");
            }
        });
        this.TestFavorite();
},

methods:{
    logout(){
      axios.delete('http://localhost:8080/api/public/logout', null, {withCredentials: true})
      sessionStorage.clear(); 
      store.commit('clearState');
      router.push({path:'/intro'})
    },
    getUserPostCount(id) {
          this.$axios.get('/api/board/get-post-count', {
            params: {
              id: id
            }
          })
          .then(response => {
            this.userData.commentCount = response.data.commentCount
            this.userData.postCount = response.data.postCount
          })
          .catch(error => {
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert("토큰이 만료되었습니다.");
            }
        });
    },
    AlterIdentity(){
      this.$emit('Alter-identity');
    },
    showGroupModal(){
      this.ShowModal = !this.ShowModal
    },
    setMessageId(id){
      this.friendWithMessageId = id;
    },
    backModal(){
      this.friendWithMessageId = null;
      this.ShowModal = false;
    },
    toFavorite() {
    this.$router.push(`/main/favorite`).then(() => {
    this.TestFavorite();
    });
    },
    TestFavorite() {
    this.IsFavorite = this.$route.path.includes('/favorite');
    },
    toMain() {
    this.$router.push(`/main`).then(() => {
      this.TestFavorite();
    });
  },

}
}
</script>

<style scoped>
header {
  
  width: 1100px;
  margin: 0 auto;
}
.container{
  width: 1100px;
  margin: 0 auto;
}
.user-info{
  width: 1000px;
  height: 130px;
  border: 1px solid rgba(108, 117, 125, 0.2);
  margin: 80px auto;
  padding: 20px;
  position: relative;
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}
.user-info h4 {
  height: 50px;
  margin-bottom: 10px;
}

.user-info > span {
  height: 50px;
  line-height: 50px;
}


.user-info p { 
  display: inline-block;
  padding-right: 20px;
  }

.header-button {
  position: absolute;
  top: 50px;
  right: 30px;
}

.header-button > div {
  text-align: center;
  float: left;
  height: 50px;
  width: 50px;
  background-color: #dc3545;
  border-radius: 50%;
}
.header-button > div:first-child {
  background-color: #5F374B;
}


.header-button div a span {
  display: inline;
  height: 50px;
  font-size: 30px;
  line-height: 50px;
  color: white;
}


.header-button > * {
  margin-left: 30px;
}
.user-info .btn {
    width: 120px;
    height: 50px;
}
.star {
  font-size: 40px;
  opacity: 0.8;
  color: navy;
  vertical-align: middle;
  margin-bottom: 9px;
  font-weight: 300;
}
.star:hover {
  cursor: pointer;
}
.fill-star{
  font-variation-settings: 'FILL' 1
}
</style>