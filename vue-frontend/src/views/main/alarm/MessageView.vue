<template>
    <div>
      <div class="form-container">
          <form @submit.prevent="submitMessage">
            <div>
                <label for="recipient" class="form-label">보낸 사람:</label>
                <input type="text" class="form-control" id="forward" :value="message.forwardNickname" disabled>
            </div>
            <div class="mb-3">
                <label for="recipient" class="form-label">받는 사람:</label>
                <input type="text" class="form-control" id="recipient" :value="message.receivedNickname" disabled>
            </div>

            <div class="mb-3">
                <textarea style="height: 200px;" disabled class="form-control" minlength="5" placeholder="메세지를 입력해 주세요." id="messageText" rows="3" v-model="message.content"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">답장하기</button>
            <button @click="toMain()" type="submit" class="btn btn-secondary">취소</button>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props :['id'],
    mounted() {
      this.getMessage();
    },
    data() {
      return {
        message: {},

      }
    },
    methods: {
      getMessage(){
        const id = this.id;
        this.$axios.get(`/api/message/get-message/${id}`)
        .then(response=>{this.message = response.data;console.log(this.message)})
        .catch(() => {alert("존재하지 않는 메세지입니다."); this.toMain()})
      },
      toMain(){
        this.$emit('CloseModal');
      },
      submitMessage(){
        const message = this.message;
        this.$emit('SubmitForm', {receivedNickname: message.forwardNickname, receiveId: message.forwardId});
      }
    }
  }
  </script>
  
  <style scoped>
 
  .form-control{

    width: 400px;
  }
  .form-container{
      width: 400px;
      height: 500px;
      border: 1px solid rgba(224, 224, 224, 0.3);
      padding: 20px;
  }
  .form-container form > * {
    margin: 10px 0;
  }
  .form-container form button {
    width: 120px;
    height: 50px;
    margin-right: 20px;
  }
  .form-container form button:last-child {
    width: 100px;
  }
  </style>
  