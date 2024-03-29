<template>
    <header v-if="!isLoading">
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
                   <a @click="showReport"><span class="material-symbols-outlined">priority_high</span></a>
               </div>
                 <div> 
                    <a><span @click="showAlarm" class="material-symbols-outlined">notifications</span></a>
                    <span v-if="alarmCount>=1" class="material-symbols-outlined plus" style="position: absolute; border-radius: 50%; background-color:#FFF455; color: brown;">add</span>
                 </div>
                 <div> 
                   <a @click="showGroupModal"><span class="material-symbols-outlined">groups</span></a>
                  </div>
                <button class="btn btn-success edit" @click="AlterIdentity" >정보수정</button>
                <button class="btn btn-secondary logout" @click="logout">로그아웃</button>
              </div>
          </div>   
      </div>
      <modal-component :show="ShowFriendModal" @close="ShowFriendModal = false"  class="">
        <app-friend @show-message="setMessageId" v-if="!friendWithMessageId"></app-friend>
        <user-message @switch-modal="backModal" v-if="friendWithMessageId"></user-message>
      </modal-component>
      <modal-component  :show="ShowAlarmModal" @close="ShowAlarmModal = false, alarmForm='list'" class="">
        <user-alarm @ToMessage="toMessage" @to-close="ShowAlarmModal = false" v-if="alarmForm=='list'"></user-alarm>
        <message-view @submit-form="ToMessageForm" @close-modal="ShowAlarmModal = false,alarmForm='list'" :id="messageId" v-if="alarmForm=='message'"></message-view>
        <user-message :receiveId="receiveId" :nickname="receivedNickname" @switch-modal="backModal" v-if="alarmForm=='send'"></user-message>
      </modal-component>

      <modal-component :show="showReportModal" @close="showReportModal = false">
        <report-by-user @admin-list="toAdminList" :role="userData.role" @close ="showReportModal = false"></report-by-user>
      </modal-component>
  </header>
  <header v-else>

  </header>
</template>
<script>
import axios from 'axios';
import router from '@/router';
import store from '@/store';
import ModalComponent from '@/components/layout/ModalComponent.vue';
import AppFriend from '@/views/main/header/AppFriend.vue';
import UserMessage from '@/views/main/alarm/UserMessage.vue';
import UserAlarm from '@/views/main/alarm/UserAlarm.vue';
import MessageView from '@/views/main/alarm/MessageView.vue';
import ReportByUser from '@/views/main/header/ReportByuser.vue';
export default {
  components :{
    ModalComponent,
    AppFriend,
    UserMessage,
    UserAlarm,
    MessageView,
    ReportByUser
  },
data(){
    return{
      receivedNickname:'',
      receiveId:null,
      messageId : null,
      alarmForm : 'list',
      alarmCount : 0,
      isLoading: true,
      IsFavorite:null,
      friendWithMessageId:null,
      showReportModal : false,
      ShowFriendModal : false,
      ShowAlarmModal : false,
      userData: {
        id : '' ,
        userid: '',
        userLevel:{level : 1, points: 0},
        createdDate: '',
        nickname: '',
        postCount:'',
        commentCount:'',
        picture:'',
        role :'',
      }
    }
},
watch: {
    '$route.path': function() {
      this.checkFavoritePath();
    }
  },
mounted(){
  this.getLoad();
  this.checkFavoritePath();
  this.getAlarm();

},

methods:{
    getLoad(){
      let loader = this.$loading.show();
      this.$axios.get('/api/member/get-user')
          .then(response => {
            this.userData = response.data;
            console.log(response.data.userLevel);
            this.getUserPostCount(this.userData.id)
            sessionStorage.setItem('userIdx',this.userData.id)
            this.isLoading = false;
            this.sendRole();
            loader.hide();
          })
          .catch(error => {
            loader.hide();
            this.isLoading = false;
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert("토큰이 만료되었습니다.");
            }
        });
    },
    getAlarm(){
        this.$axios.get('/api/alarm/new',  {withCredentials: true})
        .then((response =>{
            this.alarmCount = response.data;
        }))
    },
    checkFavoritePath() {
      this.IsFavorite = this.$route.path.includes('/favorite');
    },
    logout(){
      axios.post('http://localhost:8080/api/public/logout', null, {withCredentials: true})
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
      this.ShowFriendModal = !this.ShowFriendModal
    },
    setMessageId(id){
      this.friendWithMessageId = id;
    },
    backModal(){
      this.friendWithMessageId = null;
      this.ShowFriendModal = false;
      this.ShowAlarmModal = false;
      this.alarmForm='list';
    },
    toFavorite() {
    this.$router.push(`/main/favorite`).then(() => {
    });
    },
    toMain() {
    this.$router.push(`/main`).then(() => {
    });
  },
    showAlarm(){
      this.ShowAlarmModal = !this.ShowAlarmModal;
      this.alarmCount = 0;
    },
    toMessage(id){
      this.messageId = id;
      this.alarmForm="message";
    },
    ToMessageForm(data) {
    const { receivedNickname, receiveId } = data;
    this.alarmForm = 'send';
      this.receivedNickname = receivedNickname;
      this.receiveId = receiveId;
    },
    sendRole(){
      this.$emit('send-role',this.userData.role);
    },
    showReport(){
      this.showReportModal = true;
    },
    toAdminList(){
      this.$router.push(`/main/report-list`);
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
.header-button > div:nth-child(2) {
  background-color: #5F374B;
}

.header-button > div:nth-child(1) {
  background-color: #FFD23F;
}
.header-button > div:nth-child(1) > a span {
  color: black;
}

.header-button div a span {
  display: inline;
  height: 50px;
  font-size: 30px;
  line-height: 50px;
  color: white;
}


.header-button > * {
  margin-left: 25px;
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
@keyframes blink {
  0%, 100% { opacity: 1; }
  30% { opacity: 0; }
}

.plus {
  animation: blink 1s ease-in infinite;
}
</style>