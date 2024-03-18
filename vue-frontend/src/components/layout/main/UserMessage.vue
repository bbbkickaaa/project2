<template>
    <div>
      <h4 style="color: rgba(99, 99, 99, 1);"><span style="font-size: 30px; vertical-align: bottom; " class="material-symbols-outlined">send</span>   쪽지 보내기</h4>
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
        .then(response=>{alert(response.data); this.toMain(); this.$emit('closeModal',true); }).catch(error=>{alert(error.response.data);})
      },
      toMain(){
        this.$emit('switchModal','userInfo')
      },
    }
  }
  </script>
  
  <style scoped>
 
  .form-control{

    width: 400px;
  }
  .form-container{
      width: 500px;
      height: 400px;
      margin: 20px auto;
      border: 1px solid rgba(224, 224, 224, 0.3);
      padding: 20px;
  }
  .form-container form > * {
    margin: 30px 0;
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
  