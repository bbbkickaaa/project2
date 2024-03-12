<template>
    <div class="container mt-4 wrap">
      <div><span class="material-symbols-outlined turn-back" @click="returnMain">undo</span></div>
      <form @submit.prevent="submitPost">
        <div class="mb-3">
          <label for="titleInput" class="form-label">제목</label>
          <input type="text" class="form-control" id="titleInput" v-model="post.title" maxlength="80" :minlength="minLength" required placeholder="제목을 입력하세요">
        </div>
        <div>
          <select v-model="post.category1">
            <option value="chat">잡담</option>
            <option value="game">게임</option>
          </select>
          <select v-if="post.category1 === 'chat'" v-model="post.category2">
            <option value="chat">잡담</option>
          </select>
          <select v-if="post.category1 === 'game'" v-model="post.category2">
            <option value="lol">리그오브레전드</option>
            <option value ="overWatch">오버워치</option>
            <option value="mapleStory">메이플스토리</option>
          </select>
          <select v-model="post.category3">
            <option value="free">자유</option>
            <option value ="ask">질문</option>
            <option value="info">정보</option>
          </select>

        </div>
        <div class="mb-3">
          <textarea class="form-control contents" id="contentInput" v-model="post.content" maxlength="400" :minlength="minLength" required rows="4" placeholder="내용을 입력하세요"></textarea>
        </div>
        <div class ="sections">
            <button type="submit" class="btn btn-success">새 글 저장하기</button>
            <button type="submit" @click="returnMain" class="btn btn-secondary">취소</button>
        </div>    
      </form>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {

        //img
      postContent: '',
      selectedFile: null,
      imageUrl: null,

        post: {
          title: '',
          content: '',
          id : '',
          category1 :'chat',
          category2 : 'chat',
          category3 : 'free',
        },
        minLength : 5
      };
    },
    methods: {
      returnMain(){
        this.$router.push('/main')
        },
      submitPost() {
        if (this.post.title.length >= this.minLength && this.post.content.length >= this.minLength ) {
            this.PostServe();
        } else {
        alert(`최소 ${this.minLength} 글자를 입력해야 합니다.`);
         }
      },
       PostServe(){
        this.$axios.post('/api/board/post',this.post)
          .then(()=>{
            this.$router.push('/main')
          })
          .catch(error => {
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert("오류가 발생햇습니다.");
            }
           });
        },
        handleFileUpload(event){
          this.selectedFile = event.target.files[0];
          console.log('selectedFile:', this.selectedFile);
          this.imageUrl = URL.createObjectURL(this.selectedFile);
          console.log('imageUrl:', this.imageUrl);
    },
        
        },
    mounted() {
        this.post.id = sessionStorage.getItem('userIdx');
    },
    watch: {
    'post.category1'(newValue) {
      if (newValue === 'chat') {
        this.post.category2 = 'chat';
        this.post.category3 = 'free';
      } else if (newValue === 'game') {
        this.post.category2 = 'lol';
        this.post.category3 = 'free';
      }
    }
    },
  }

  </script>
  <style scoped>
    .wrap {
        margin: 0 auto;
        width: 1000px !important;
        border: 1px solid rgba(108, 117, 125, 0.2);
        height: 800px;
        margin-bottom: 300px;
        border-radius: 10px;
        box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
        padding: 20px;
    }
    .contents{
        height: 500px;
    }
    .form-label {
        padding-left: 10px;
    }

    .sections{
        padding-top: 20px;
        width :1000px;
        display: flex;
    }
    .sections button {
        margin-left: 20px;
        width: 160px;
        height: 50px;
    }
    .sections button:last-child {
      width: 120px;
    }
    .turn-back{
        margin-left:20px; 
        font-size: 50px; 
        color: #999999;
    }
    .turn-back:hover {
        color: black;
        cursor: pointer;
    }

  </style>