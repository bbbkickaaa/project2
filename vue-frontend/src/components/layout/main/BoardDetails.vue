<template>
    <div class="wrap">
        <div class="container mt-4 contentWrap">
        <div class="post-detail innerBox">
            <h2 class="post-title titles">{{ BoardInfo.title  }}</h2>
            <p class="post-content contents">{{ BoardInfo.content }}</p>
            <div class="post-info">
            </div>
        </div>
        </div>
            <div class="actions">
            <button class="btn btn-secondary"  v-if="CompareWithUsers()" @click="ToMain">수정</button>
            <button class="btn btn-secondary" v-if="CompareWithUsers()" @click="ShowModal = true;" @close="closeModal">삭제</button>
            <button class="btn btn-secondary" @click="ToMain">돌아가기</button>
            </div>
        
        <!-- 댓글 입력 폼 -->
        <div class="comment-form mt-4">
            <textarea class="form-control"  placeholder="댓글을 입력하세요" rows="3"></textarea>
            <button class="btn btn-primary mt-2" >댓글 작성</button>
        </div>
    
        <!-- 댓글 리스트 -->
        <div class="comment-list mt-4">
            <div class="comment" >
            <p class="comment-content">{{ }}</p>
            </div>
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
  </template>
  <script>
  import ModalComponent from '../ModalComponent.vue';
  export default {
    components: {
         ModalComponent
        },
    data(){
        return{
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
                comments:[]
            }
        }
    },
mounted(){
    const id = this.$route.query.id;
    this.$axios.get('/api/board/getDetail', {
      params: {
          id: id
      }})
    .then(response => {
        console.log(response.data)
            this.BoardInfo = response.data;
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

    this.userIdx = sessionStorage.getItem('userIdx');
    },
watch: {
    ShowModal(newValue) {
        if (newValue) {
        this.showSignupModal = false;
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
    this.$axios.post('/api/board/postViews', this.BoardInfo.id)
  },
  DeleteBoard(){
    this.$axios.post('/api/board/deleteBoard', this.BoardInfo.id).then(this.$router.push('/main')).catch(error => {
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert(`${error.message}`);
            }})
        }
    }
}
</script>
<style scoped>
    .wrap {
        margin: 0 auto;
        width: 1000px !important;
        border: 1px solid rgba(108, 117, 125, 0.5);
        min-height: 1100px;
        padding-top: 50px;
        margin-bottom: 300px;
    }
    .contentWrap{
        height: 700px;
    }

    .innerBox{
        margin: 0 auto;
        width: 900px;
        border: 1px solid rgba(108, 117, 125, 0.2);
        padding: 30px;
    }
    .contents{
        padding: 20px;
        height: 500px;
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
</style>