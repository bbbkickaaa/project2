<template>
    <div class="container mt-4 wrap">
      <form @submit.prevent="">
        <div class="mb-3">
          <label for="titleInput" class="form-label">제목</label>
          <input type="text" class="form-control" id="titleInput" v-model="post.title" maxlength="80" required placeholder="제목을 입력하세요">
        </div>
        <div class="mb-3">
          <label for="contentInput" class="form-label">내용</label>
          <textarea class="form-control contents" id="contentInput" v-model="post.content"  maxlength="400"  required rows="4" placeholder="내용을 입력하세요"></textarea>
        </div>
        <div class ="sections">
            <button type="submit" @click="AlterBoard(id)" class="btn btn-secondary">수정</button>
            <button type="submit" @click="returnBoardDetail(id)" class="btn btn-secondary">취소</button>
        </div>    
      </form>
    </div>
  </template>

<script>
export default {
    data(){
        return{
            id : null,
            post: {
             title: '',
             content: '',
             id : '',
             boardId:''
            },
            minLength : 5
        }
    },
    methods: {
      returnBoardDetail(id){
        this.$router.push(`/main/detail/${id}`)
        },
        AlterBoard(id){
        if (this.post.title.length >= this.minLength && this.post.content.length >= this.minLength ) {
            this.$axios.put('/api/board/alter',this.post).then(()=>
                {this.$router.push(`/main/detail/${id}`)})
        }
        },
        getDetail(){
          this.$axios.get('/api/board/get-detail-only-alter', { params: { id: id }})
        .then(Response => {this.post.title = Response.data.title; this.post.content = Response.data.content });
        },
    },
    mounted(){
        const id = parseInt(this.$route.params.id, 10);
        this.id = id;
        this.post.boardId = this.$route.params.id;
        this.post.id = sessionStorage.getItem('userIdx');
        this.getDetail();
    }


}
</script>


<style scoped>
    .wrap {
        margin: 0 auto;
        width: 1000px !important;
        border: 1px solid rgba(108, 117, 125, 0.5);
        height: 800px;
        padding-top: 50px;
        margin-bottom: 300px;
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
        width: 80px;
        height: 37px;
    }

  </style>