<template>
    <div class="container mt-4 wrap">
      <form @submit.prevent="submitPost">
        <div class="mb-3">
          <label for="titleInput" class="form-label">제목</label>
          <input type="text" class="form-control" id="titleInput" v-model="post.title" maxlength="80" :minlength="minLength" required placeholder="제목을 입력하세요">
        </div>
        <div class="mb-3">
          <textarea class="form-control contents" id="contentInput" v-model="post.content" maxlength="400" :minlength="minLength" required rows="4" placeholder="내용을 입력하세요"></textarea>
        </div>
        <div class ="sections">
            <button type="submit" class="btn btn-primary">새 글 저장하기</button>
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
            alert('폼 제출 성공!');
            this.$router.push('/main')

        } else {
        alert(`최소 ${this.minLength} 글자를 입력해야 합니다.`);
         }
      },
       PostServe(){
        this.$axios.post('/api/board/post',this.post)
          .then(response => {
            console.log(response.data)
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
    
    }

  </script>
  <style scoped>
    .wrap {
        margin: 0 auto;
        width: 1000px !important;
        border: 1px solid rgba(108, 117, 125, 0.2);
        height: 800px;
        padding-top: 50px;
        margin-bottom: 300px;
        border-radius: 10px;
        box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
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

  </style>