<template>
    <div>
      <h3>쪽지 보내기</h3>
      <div class="form-container">
          <form @submit.prevent="submitMessage">
            <div class="mb-3">
                <label for="recipient" class="form-label">받는 사람:</label>
                <input type="text" class="form-control" id="recipient" :value="nickname" :disabled="nickname">
            </div>
            <div class="mb-3">
                <label for="messageText" class="form-label">메시지:</label>
                <textarea class="form-control" id="messageText" rows="3" v-model="message.content"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">보내기</button>
            <button @click="toMain()" type="submit" class="btn btn-secondary">취소</button>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props :['nickname','receiveId'],
    data() {
      return {
        message: {
          receiveId:this.receiveId,
          content: ''
        }
      }
    },
    methods: {
      submitMessage() {
        // 메시지 제출 로직
        console.log("보낸 사람:", this.message.receiveId);
        console.log("메시지 내용:", this.message.content);
        this.$axios.post('/api/message/post-message',this.message,{withCredentials: true})
        .then(response=>{alert(response.data); this.toMain(); this.$emit('closeModal',true); })
      },
      toMain(){
        this.$emit('switchModal','userInfo')
      },
    }
  }
  </script>
  
  <style scoped>
  button {
    width: 120px;
    height: 50px;
  }
  .form-control{
    width: 400px;
  }
  .form-container{
      width: 500px;
      height: 300px;
      margin: 40px auto;
  }
  </style>
  