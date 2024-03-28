<template>
    <div class="container mt-4 wrap">
      <div><span class="material-symbols-outlined turn-back" @click="returnMain">undo</span></div>
      <form @submit.prevent="submitPost">
        <div class="mb-3">
          <input type="text" class="form-control" id="titleInput" v-model="post.title" maxlength="80" :minlength="minLength" required placeholder="제목을 입력하세요">
        </div>
        <div>
          <select v-model="post.category1" class="editor-select">
              <option value="chat">잡담</option>
              <option value="game">게임</option>
              <option value="beauty">뷰티</option>
              <option value="study">공부</option>
              <option value="travel">여행</option>
          </select>
          <select v-if="post.category1 === 'chat'" v-model="post.category2" class="editor-select">
            <option value="chat">잡담</option>
            <option value="common">일상</option>
            <option value="star">연예</option>
            <option value="love">사랑</option>
            <option value="food">음식</option>
          </select>
          <select v-if="post.category1 === 'game'" v-model="post.category2" class="editor-select">
            <option value="lol">리그오브레전드</option>
            <option value ="overwatch">오버워치</option>
            <option value="maplestory">메이플스토리</option>
            <option value="varolant">발로란트</option>
            <option value="mabinogi">마비노기</option>
          </select>
          <select v-if="post.category1 === 'beauty'" v-model="post.category2" class="editor-select">
            <option value="makeup">화장</option>
            <option value ="fashion">패션</option>
            <option value="skin">피부</option>
            <option value="diet">다이어트</option>
            <option value="hairstyle">헤어스타일</option>
          </select>
          <select v-if="post.category1 === 'study'" v-model="post.category2" class="editor-select">
            <option value="certification">자격증</option>
            <option value ="suneung">수능</option>
            <option value="toeic">토익</option>
            <option value="hobby">취미</option>
            <option value="interview">면접</option>
          </select>
          <select v-if="post.category1 === 'travel'" v-model="post.category2" class="editor-select">
            <option value="oversea">해외</option>
            <option value ="domestic">국내</option>
            <option value="festival">축제</option>
            <option value="event">이벤트</option>
          </select>

          <select v-model="post.category3" class="editor-select">
            <option value="free">자유</option>
            <option value ="ask">질문</option>
            <option value="info">정보</option>
          </select>
        </div>
        <tip-tap v-model="post.content" @value="startPost" :stringList="picturesUrl" @imageLoaded="handleImageLoaded" class="tip-tap"/>
        <div class ="sections">
            <button type="submit" class="btn btn-success">새 글 저장하기</button>
            <button type="submit" @click="returnMain" class="btn btn-secondary">취소</button>
        </div>    
      </form>
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
        pictures : [],
        picturesUrl : [],
        post: {
          title: '',
          content: '',
          id : '',
          boardId : '',
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
        if (this.post.title.length >= this.minLength ) {
            this.PostServe();
        } else {
        alert(`최소 ${this.minLength} 글자를 입력해야 합니다.`);
         }
      },
       PostServe(){
        if(this.pictures.length >=1 ){
          let formData = new FormData();
          this.pictures.forEach(file => {
          formData.append('files', file);
          });
          this.$axios.post('/api/file/board-img', formData, {
            withCredentials: true,
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          }).then((response) => {
            const arrayList = [];
            this.post.boardId = response.data.id
            response.data.boardImage.forEach(image => {
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
        }

        
        },
      startPost(newVal){
        this.post.content = newVal;
          this.$axios.post('/api/board/post',this.post).then(()=>{
            this.$router.push('/main');
          })
        },

    handleImageLoaded(file){
      this.pictures.push(file);
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
      } else if (newValue === 'beauty') {
        this.post.category2 = 'makeup';
        this.post.category3 = 'free';
      }else if (newValue === 'study') {
        this.post.category2 = 'toeic';
        this.post.category3 = 'free';
      }else if (newValue === 'travel') {
        this.post.category2 = 'oversea';
        this.post.category3 = 'free';}
  }
}
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