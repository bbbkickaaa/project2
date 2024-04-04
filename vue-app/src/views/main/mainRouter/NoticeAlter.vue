<template>
    <div class="container mt-4 wrap">
      <div><span class="material-symbols-outlined turn-back" @click="returnMain">undo</span></div>
      <form @submit.prevent="submitPost">
        <div class="mb-3">
          <input type="text" class="form-control" id="titleInput" v-model="notice.title" maxlength="80" :minlength="minLength" required placeholder="제목을 입력하세요">
        </div>
        <div class ="sections">
          <button type="submit" class="btn btn-success">새 글 저장하기</button>
          <button type="submit" @click="returnMain" class="btn btn-secondary">취소</button>
        </div>    
      </form>
      <tip-tap :existContent="notice.content" @value="startPost" :stringList="picturesUrl" :readyToPost="readyToPost" @imageLoaded="handleImageLoaded" class="tip-tap"/>
    </div>
  </template>
  
  <script>
import TipTap from '@/components/TipTap.vue';
  export default {
    components :{
      TipTap,
    },
    data() {
      return {
        id : null,
        readyToPost : false,
        pictures : [],
        picturesUrl : [],
        notice: {
          title: '',
          content: '',
          userIdx : '',
          noticeId : '',
        },
        minLength : 5
      };
    },
    methods: {
      returnMain(){
        this.$router.push('/main')
        },
      submitPost() {
        
        if (this.notice.title.length >= this.minLength ) {
            let submit = false;
            if(!submit){
            this.readyToPost = true;
            submit = true;}
        } else {
        alert(`최소 ${this.minLength} 글자를 입력해야 합니다.`);
         }

      },
      PostServe(){
          if(this.pictures.length >= 1){
            let formData = new FormData();
            this.pictures.forEach((item) => {
              formData.append('files', item.file);
            });

            formData.append('noticeId',this.notice.noticeId);
            let attrs = this.pictures.map(item => item.attr);
            formData.append('attrs', JSON.stringify(attrs));
          this.$axios.post('/api/file/notice-imga', formData, {
            withCredentials: true,
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          }).then((response) => {
            const arrayList = [];
            response.data.noticeImage.forEach(image => {
            arrayList.push(image.filePath);
          })
          this.picturesUrl = arrayList;
          }).catch(error => {
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert("오류가 발생했습니다.");

            }
          })
        }else{
          const uniqueIdentifier = "\u{1F4A9}\u{1F4A3}\u{1F4A5}\u{1F4AB}\u{1F4A2}";
          const arrayList = [];
          arrayList.push(uniqueIdentifier);
          this.picturesUrl.push(arrayList);
        }

        
        },
      startPost(newVal){
        this.notice.content = newVal;
          this.$axios.put('/api/notice/alter',this.notice,{
            withCredentials: true}).then(()=>{
            this.$router.push('/main');
          })
        },

    handleImageLoaded(files){
      this.pictures = files;
      this.PostServe();
    },
    returnBoardDetail(id){
        this.$router.push(`/main/detail/${id}`)
        },
        getDetail(id){
          this.$axios.get('/api/notice/get-detail-only-alter', { params: { id: id }})
        .then(Response => {this.notice = Response.data; console.log(this.notice.content)})
        .catch(()=>{this.$router.push('/main')});
        },

  },

    mounted() {
        const id = parseInt(this.$route.params.noticeId, 10);
        const StringId = this.$route.params.noticeId;
        this.id = id;
        this.notice.noticeId = StringId;
        this.notice.userIdx = sessionStorage.getItem('userIdx');
        this.getDetail(id);
    },
  }

  </script>
  <style scoped lang="scss">
    .wrap {
        margin: 0 auto;
        width: 1000px !important;
        border: 1px solid rgba(108, 117, 125, 0.2);
        min-height: 1100px;
        margin-bottom: 300px;
        border-radius: 10px;
        box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
        padding: 50px;
        position:relative;
    }
    .contents{
        height: 500px;
    }
    .form-label {
        padding-left: 10px;
    }
    #titleInput{
      height: 43px;
      margin-top : 30px;
    }

    .sections{
      position: absolute;
      bottom : 50px;
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
        font-size: 50px; 
        color: #999999;
    }
    .turn-back:hover {
        color: black;
        cursor: pointer;
    }
    .tip-tap {
      margin-top: 20px;

      min-height:700px;
    }

    
    .tiptap {
  > * + * {
    margin-top: 0.75em;
  }

  code {
    background-color: rgba(#616161, 0.1);
    color: #616161;
  }
}

.editor-select {
  color : white;
  height : 43px;
  padding: 8px;
  margin-right: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #198754;
  cursor: pointer;
  margin-bottom : 20px;
}

.editor-select:hover {
  opacity: 0.9;
  background-color: green;
  color : red;
}
.content {
  padding: 1rem 0 0;

  h3 {
    margin: 1rem 0 0.5rem;
  }

  pre {
    border-radius: 5px;
    color: #333;
  }

  code {
    display: block;
    white-space: pre-wrap;
    font-size: 0.8rem;
    padding: 0.75rem 1rem;
    background-color:#e9ecef;
    color: #495057;
  }
}

    

  </style>