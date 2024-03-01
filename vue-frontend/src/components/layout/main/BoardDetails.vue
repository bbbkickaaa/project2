<template>
    <div class="wrap">
        <div class="container mt-4 contentWrap">
            <div class="post-detail innerBox">
                <div class="post-info">
                    <ul>
                        <li class="infos">작성자 : {{ BoardInfo.nickname }}</li>
                        <li class="infos">조회수 : {{ BoardInfo.views }}</li>
                        <li class="infos">작성일 : {{ BoardInfo.writeDate }}</li>
                        <li v-if="BoardInfo.alterDate" class="infos">수정일 : {{ BoardInfo.alterDate }}</li>
                    </ul>
                </div>
                <h3 class="post-title titles">{{ BoardInfo.title  }}</h3>
                <p class="post-content contents">{{ BoardInfo.content }}</p>
                <div class = "recommend">
                    <button class="btn btn-primary recommendButton" :disabled="IsRecommended" @click="PostRecommend"><span>추천 : {{BoardInfo.likes }}</span></button>
                </div>
            </div>
                
            <div class="actions">
                <button class="btn btn-secondary" v-if="CompareWithUsers()" @click="AlterBoard(id)">수정</button>
                <button class="btn btn-secondary" v-if="CompareWithUsers()" @click="ShowModal = true;" @close="closeModal">삭제</button>
                <button class="btn btn-secondary" @click="ToMain">돌아가기</button>
            </div>
        
        <!-- 댓글 입력 폼 -->
            <form @submit.prevent="PostComment">
                <div class="comment-form mt-4" >
                    <textarea v-model="CommentForm.comment" class="form-control" maxlength="80"  placeholder="댓글을 입력하세요" rows="3"></textarea>
                    <button class="btn btn-primary mt-2" :disabled="CommentForm.comment.length == 0" >댓글 작성</button>
                </div>
            </form>
        </div>
            <modal-component :show="ShowModal" @close="ShowModal = false" class="">
                <div class="modalInner">
                <h4 style="text-align: center; padding-top: 50px;">정말 삭제하시겠습니까?</h4>
                    <div class="ButtonOuter">
                        <button class="btn btn-secondary modalButton" @click="DeleteBoard">네</button>
                        <button class="btn btn-secondary modalButton" @click="ShowModal = false">취소</button>
                    </div>
                </div>
            </modal-component>
        </div>  
    <!-- 댓글 리스트 -->
    <div class="comment-list mt-4" v-if="BoardInfo.comments">
        <div class="comment-box">
            <div class="comment" v-for="(list,index) in BoardInfo.comments" :key="list.commentId" >
                <p class="comment-nickname"><span class="comment-idx">{{ index + 1 }}.       
                </span>{{  list.nickname  }} <label class="comment-writeDate">{{ '(' + list.writeDate + ')' }}</label></p>
                
                <p class="comment-content">{{list.content}}</p>
                <a v-if="list.userid == userIdx" @click="DeleteComment(list.commentId,BoardInfo.id)" class="comment-delete">삭제</a>
            </div>
        </div>
    </div>
  </template>
  <script>
  import ModalComponent from '../ModalComponent.vue';
  export default {
    components: {
         ModalComponent
        },
    data(){
        return{
            CommentForm:{
                BoardId : '',
                userIdx : '',
                comment : ''
            },
            IsRecommended : false,
            ShowModal: false,
            userIdx : '',
            id: null,
            BoardInfo : {
                title :'',
                content:'',
                id:'',
                likes:'',
                nickname:'',
                userIdx : '',
                views : '',
                writeDate : '',
                alterDate : '',
                comments:[]
            }
        }
    },
mounted(){
    const id = parseInt(this.$route.params.id, 10);
    this.$axios.get('/api/board/get-detail', { params: { id: id }})
    .then(response => {
            console.log(response.data)
            this.BoardInfo = response.data;
            this.CommentForm.BoardId = response.data.id;
            this.PlusViews();
          })
          .catch(error => {
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert(`${error.message}`);
            }
        });
    this.id = id;
    this.userIdx = sessionStorage.getItem('userIdx');
    this.CommentForm.userIdx = this.userIdx;
    },
watch: {
    ShowModal(newValue) {
        if (newValue) {
        this.ShowModal = false;
        }
    }
},
methods: {
  ToMain(){
    this.$router.push('/main');
  },
  CompareWithUsers(){
    return this.userIdx == this.BoardInfo.userIdx;
  },
  PlusViews(){
    this.$axios.post('/api/board/post-views', this.BoardInfo.id)
  },
  DeleteBoard(){
    this.$axios.post('/api/board/delete-board', this.BoardInfo.id)
    .then(()=> {this.$router.push('/main')})
    .catch(error => {
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert(`${error.message}`);
            }})
        },
  PostRecommend (){
    this.$axios.post('/api/board/post-recommend', this.BoardInfo)
    .then(()=>{this.BoardInfo.likes += 1, this.IsRecommended = true})
    .catch((response)=>{alert(response.response.data);})
  },
  PostComment(){
    this.$axios.post('/api/board/post-comment', this.CommentForm)
    .then(response => {
      this.CommentForm.comment = '';
      this.BoardInfo.comments.push(response.data[response.data.length - 1])
    })
    },
    DeleteComment(commentId,boardId){
    this.$axios.post('/api/board/delete-comment', {commentId,boardId})
    .then(()=>this.BoardInfo.comments = this.BoardInfo.comments.filter(comment => comment.commentId !== commentId))
    },
    AlterBoard(id) {
    this.$router.push({ name: 'BoardAlter', params: { id: id } });
    }
}
}

</script>
<style scoped>
    .wrap {
        margin : 0 auto;
        width : 1000px !important;
        border : 1px solid rgba(108, 117, 125, 0.5);
        min-height : 1200px;
        padding-top : 50px;
        margin-bottom: 100px;
    }
    .contentWrap{
        height : 1100px;
    }

    .recommend{
        position: relative;
        display: flex;
        justify-content: center;
    }
    .recommend .recommendButton{
        position: absolute;
        top: 20px;
        width: 150px;
        height: 50px;

    }

    .innerBox{
        margin: 0 auto;
        width: 900px;
        border: 1px solid rgba(108, 117, 125, 0.2);
        padding: 30px;
        height: 800px;
        margin-bottom: 50px;
    }
    .contents{
        padding: 20px;
        height: 400px;
    }
    .actions {
        display: inline-block;
        text-align: right;
        width: 1000px;
        height: 50px;
    }
    .actions .btn {
        margin-right: 20px;
    }
    .actions .btn:last-child {
        margin-right: 40px;
    }
    .titles{
        line-height: 50px;
        border-bottom: 1px solid rgba(108, 117, 125, 0.2);
        height: 80px;
        padding-top: 15px;
        padding-left: 10px;
    }
    .modalInner{
        box-sizing: border-box;
        padding: 30px;
        border: 1px solid rgba(108, 117, 125, 0.2);
        height: 400px;
        margin-top: 30px;
    }
    .modalButton{
        margin: 0 auto;
        width: 100px;
        margin-right: 20px;
    }
    .ButtonOuter{
        box-sizing: border-box;
        border-top: 1px dashed rgba(108, 117, 125, 0.2);
        margin-left: 100px;
        margin: 0 auto;
        padding-top: 30px;
        margin-top: 180px;
        height: 150px;
        padding-left: 130px;
    }

    .post-info {
    text-align: right; 
    font-family: Arial, sans-serif; 
    color: #888; 
    padding: 5px;
    border-bottom: 1px dashed rgba(108, 117, 125, 0.2);
    height: 150px;
    }

    .post-info ul {
       
        list-style: none; 
        padding: 0; 
    }

    .infos {
        margin-bottom: 10px; 
        font-size: 16px; 
    }

    .comment-list{
        min-height: 300px;
    }
    .comment-box{
        width: 1000px;
        border: 1px solid rgba(108, 117, 125, 0.5);
        margin: 0 auto;
        margin-bottom: 150px;
    }
    .comment{
        margin: 0 auto;
        width: 900px;
        height: 200px;
        padding: 10px;
        padding-top: 50px;
        border-top: 1px dashed rgba(108, 117, 125, 0.2);
    }
    .comment-nickname{
        padding-top: 20px;
        padding-left: 10px;
        font-weight: bold;
        font-size: 20px;
    }
   .comment-idx {
    padding-right: 10px;
   }
    .comment-delete{
        color: #888;
        text-decoration: none;
        float: right;
    }
    .comment-content{
        padding-top: 5px;
        padding-left: 20px;
    }
    .comment-delete:hover{
        cursor: pointer;
    }
    .comment-writeDate{
        padding-left: 20px;
        color: #888;
        font-size: 14px;
        font-weight: lighter;
    }
</style>