<template>
    <div class="wrap">
        <div class="symbol">
            <span class="material-symbols-outlined symbols turn-back" @click="ToMain">undo</span>
            <span class="material-symbols-outlined symbols heart" v-if="!IsRecommended" @click="PostRecommend">favorite</span>
            <span class="material-symbols-outlined symbols heart fill-heart" v-if="IsRecommended" @click="PostRecommend">favorite</span>
            <span class="material-symbols-outlined symbols star" v-if="!IsFavorite" @click="PostFavorite">star</span>
            <span class="material-symbols-outlined symbols star fill-star" v-if="IsFavorite" @click="PostFavorite">star</span>
            <span @click="URLModalOpen" class="material-symbols-outlined symbols airplane ">near_me</span>
        </div>
        <div class="container mt-4 content-wrap">
            <div class="post-detail inner-box">
                <div class ="post-title ">
                    <ul class="infos">
                        <li v-if="BoardInfo.alterDate">수정 : {{ formAlterDate }}</li>
                        <li >{{ formWriteDate }}</li>
                    </ul>
                    <div class="user">
                        <img :src="BoardInfo.picture" style="width: 60px; height: 60px; border-radius: 50%; border: 3px solid darkolivegreen;" alt="user-picture">
                        <p class="author">{{ BoardInfo.nickname }} <span class="badge bg-secondary">{{ BoardInfo.level }}</span></p>
                    </div>
                    <h3 class="titles">{{ BoardInfo.title  }}</h3>
                    <div class="emoticon">
                        <p style="color: #999999;" class="count"><span style="vertical-align: bottom;" class="material-symbols-outlined">visibility</span> {{ BoardInfo.views }}</p>
                        <p style="color: #999999;" class="content-count"><span style="vertical-align: bottom;"  class="material-symbols-outlined">chat</span> {{ BoardInfo.comments.length }}</p>
                    </div>
                    <p class ="category" v-if="BoardInfo.category"> 
                        <a> {{ getKoreanCategoryName(BoardInfo.category.category1,1) }} </a>
                        <span style="vertical-align: bottom;" class="material-symbols-outlined">arrow_right_alt</span>
                        <a> {{ getKoreanCategoryName(BoardInfo.category.category2,2) }} </a>
                        <span style="vertical-align: bottom;"  class="material-symbols-outlined">arrow_right_alt</span>
                        <a> {{ getKoreanCategoryName(BoardInfo.category.category3,3) }} </a>
                    </p>
                 </div>
                <p class="post-content contents" v-html="BoardInfo.content"></p>
            </div>
                
            <div class="actions">
                <button class="btn btn-secondary" v-if="CompareWithUsersAlter()" @click="AlterBoard(id)">수정</button>
                <button class="btn btn-secondary" v-if="CompareWithUsersDelete()" @click="ShowModal = true" @close="closeModal">삭제</button>
            </div>
        
        <!-- 댓글 입력 폼 -->
            <form @submit.prevent="PostComment">
                <div class="comment-form mt-4" >
                    <textarea v-model="CommentForm.comment" class="form-control" maxlength="80"  placeholder="댓글을 입력하세요" rows="3"></textarea>
                    <button class="btn btn-success mt-2" :disabled="CommentForm.comment.length == 0" >댓글 작성</button>
                </div>
            </form>
        </div>
        </div>  
    <!-- 댓글 리스트 -->
    <div class="comment-list mt-4" v-if="BoardInfo.comments.length>0">
        <div class="comment-box" v-if="BoardInfo.comments">
            <div class="comment" v-for="(list,index) in BoardInfo.comments" :key="list.commentId" >
                <p class="comment-nickname"><span  class="comment-idx">{{ index + 1 }}. </span>
                <span><img :src="list.picture" style="width: 40px; height: 40px; border-radius: 50%; border: 3px solid darkolivegreen;" alt="list-picture"></span>
                <span style="vertical-align: bottom; margin-left: 10px;">{{  list.nickname }}<span style="font-size: 10px;margin-left: 4px; vertical-align: middle;" class="badge bg-secondary">{{ list.level  }}</span><label class="comment-writeDate">{{ '(' + formatDate(list.writeDate) + ')' }}</label></span> </p>
                
                <p class="comment-content">{{list.content}}</p>
                <a v-if="list.userid == userIdx" @click="DeleteComment(list.commentId,BoardInfo.id)" class="comment-delete">삭제</a>
            </div>
        </div>
    </div>

    <modal-component :show="ShowModal" @close="ShowModal = false" class="">
                <div class="modal-inner">
                <h4 style="text-align: center; padding-top: 50px;">정말 삭제하시겠습니까?</h4>
                    <div class="button-outer">
                        <button class="btn btn-secondary modal-button" @click="DeleteBoard">네</button>
                        <button class="btn btn-secondary modal-button" @click="ShowModal = false">취소</button>
                    </div>
                </div>
    </modal-component>
    <modal-component :show="ShowModalURL" @close="ShowModalURL=false">
        <share-url></share-url>
    </modal-component>
  </template>
  <script>
import ModalComponent from '@/components/layout/ModalComponent.vue';
import ShareUrl from '@/views/main/mainRouter/Details/ShareComponent.vue';
import { toRaw } from 'vue';
import DOMPurify from 'dompurify';
  export default {
    components: {
         ModalComponent,
         ShareUrl
        },
    props:['role'],
    data(){
        return{
            category1Mapping: {
            'chat': '잡담',
            'game': '게임',
            'beauty': '뷰티',
            'study': '공부',
            'travel': '여행',
        },
        category2Mapping:{
            'chat' : '잡담',            
            'common': '일상',
            'star': '연예',
            'love': '사랑',
            'food': '음식',
            'lol': '리그오브레전드',
            'overwatch': '오버워치',
            'maplestory': '메이플스토리',
            'valorant': '발로란트',
            'mabinogi': '마비노기',
            'makeup': '화장',
            'fashion': '패션',
            'skin': '피부',
            'diet': '다이어트',
            'hairstyle': '헤어스타일',
            'certification': '자격증',
            'suneung': '수능',
            'toeic': '토익',
            'hobby': '취미',
            'interview': '면접',
            'oversea': '해외',
            'domestic': '국내',
            'festival': '축제',
            'event': '이벤트'
        },
        category3Mapping:{
            'ask' : '질문',
            'info' :'정보',
            'free' : '자유',
        },

            ShowModalURL : false,
            formWriteDate : '',
            formAlterDate : '',
            formContentDate : '',
            CommentForm:{
                BoardId : '',
                userIdx : '',
                comment : '',
                whereParam :'',
            },
            IsFavorite : null,
            IsRecommended : null,
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
                picture:'',
                comments:[],
                level:null,
                category:[],
                likesUser:{},
                favorite:null,
                role:'',
            }
        }
    },
mounted(){
    this.CommentForm.whereParam = this.$route.path;
    const id = parseInt(this.$route.params.id, 10);
    this.id = id;
    this.getDetail(id);
    this.userIdx = sessionStorage.getItem('userIdx');
    this.CommentForm.userIdx = this.userIdx;
    },
beforeRouteUpdate(to, from, next) {
    const newId = to.params.id;
    this.getDetail(newId);
  next();
},
methods: {
  getDetail(id){
    this.$axios.get('/api/board/get-detail', { params: { id: id }, withCredentials: true})
    .then(response => {
            this.BoardInfo = response.data;
            this.CommentForm.BoardId = response.data.id;
            this.BoardInfo.content = DOMPurify.sanitize(response.data.content);
            this.PlusViews();
            this.checkRecommend();
            this.checkFavorite();
            if(this.BoardInfo.writeDate){
            this.formWriteDate = this.formatDate(this.BoardInfo.writeDate);}
            if(this.BoardInfo.alterDate){
            this.formAlterDate = this.formatDate(this.BoardInfo.alterDate);}
          })
          .catch(error => {
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert(`${error.message}`);
            }
            this.$router.push('/main');
        });
  },
  ToMain(){
    this.$router.push('/main');
  },
  CompareWithUsersAlter(){
    return this.userIdx == this.BoardInfo.userIdx
  },
  CompareWithUsersDelete(){
    return this.userIdx == this.BoardInfo.userIdx || this.role =="ADMIN";
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
    this.$axios.post('/api/board/post-recommend', this.CommentForm)
    .then(()=>{this.IsRecommended = !this.IsRecommended;})
    .catch((response)=>{alert(response.response.data);})
  },
  PostFavorite(){
    this.$axios.post('/api/board/post-favorite', this.CommentForm)
    .then(()=>{this.IsFavorite = !this.IsFavorite;})
    .catch((response)=>{alert(response.response.data);})
  },
  PostComment(){
    this.$axios.post('/api/board/post-comment', this.CommentForm)
    .then(response => {
      this.CommentForm.comment = '';
      response.data[response.data.length-1].picture = this.BoardInfo.picture;
      response.data[response.data.length-1].level = this.BoardInfo.level;
      this.BoardInfo.comments.push(response.data[response.data.length - 1])
    })
    },
    DeleteComment(commentId,boardId){
    this.$axios.post('/api/board/delete-comment', {commentId,boardId})
    .then(()=>this.BoardInfo.comments = this.BoardInfo.comments.filter(comment => comment.commentId !== commentId))
    },
    AlterBoard(id) {
    this.$router.push({ name: 'BoardAlter', params: { id: id } });
    },
    formatDate(dateStr) {
      const year = dateStr.slice(0, 4);
      const month = dateStr.slice(4, 6);
      const day = dateStr.slice(6, 8);
      const hour = dateStr.slice(8, 10);
      const minute = dateStr.slice(10, 12);
      const second = dateStr.slice(12, 14);
      return `${year}년 ${month}월 ${day}일 ${hour}:${minute}:${second}`;
    },
    checkRecommend() {
    this.IsRecommended = this.BoardInfo.likesUser.some(userId => 
        userId === Number.parseInt(this.userIdx)
    )},
    checkFavorite() {
        if(this.BoardInfo.favorite){
            this.IsFavorite = true;

        }
    },
    URLModalOpen(){
        this.ShowModalURL = !this.ShowModalURL
    },
    getKoreanCategoryName(category, type) {
        const rawCategory = toRaw(category);
        if (!rawCategory) {
            return '';
        }
        let categoryName = '';
        if (type === 1) {
            categoryName = rawCategory;
        } else if (type === 2) {
            categoryName = rawCategory;
        } else if (type === 3) {
            categoryName = rawCategory;
        }

        let mapping = {};
        if (type === 1) {
            mapping = this.category1Mapping;
        } else if (type === 2) {
            mapping = this.category2Mapping;
        } else if (type === 3) {
            mapping = this.category3Mapping;
        }
        return mapping[categoryName] || categoryName;
    },
    
} 

  }

</script>
<style scoped>
    .wrap {
        margin : 0 auto;
        width : 1000px !important;
        border : 1px solid rgba(108, 117, 125, 0.2);
        min-height : 1200px;
        padding-top : 30px;
        margin-bottom: 100px;
        box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
        border-radius: 10px;
    }
    .content-wrap{
        min-height : 1150px;
    }
    .inner-box{
        margin: 0 auto;
        width: 950px;
        padding: 10px;
        min-height: 800px;
    }
    .contents{
        margin-left: 30px;
        padding: 30px;
        min-height: 600px;
    }
    .actions {
        text-align: right;
        width: 900px;
        height: 50px;
        margin: 0 auto;

    }
    .actions .btn {
        width: 120px;
        height: 50px;
        margin-left: 20px;
    }
    .post-title{
        position: relative;
        border-bottom: 1px solid rgba(108, 117, 125, 0.2);
        height: 240px;
        padding-left: 10px;
        margin-left: 50px;
    }
    .titles{
        margin-bottom: 10px;
    }
    .modal-inner{
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
    .button-outer{
        box-sizing: border-box;
        border-top: 1px dashed rgba(108, 117, 125, 0.2);
        margin: 0 auto;
        margin-top: 180px;
        height: 40px;
        padding-top: 40px;
        text-align: center;
    }
    .infos {
 
        text-align: right;
        position: absolute;
        font-size: 16px;
        font-family: Arial, sans-serif; 
        color: #888; 
        right: 0;
        list-style: none; 
        bottom: 0px;
    }

    .comment-list{
        min-height: 300px;
    }
    .comment-box{
        background-color: white;
        width: 1000px;
        border: 1px solid rgba(108, 117, 125, 0.2);
        margin: 0 auto;
        margin-bottom: 150px;
        box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
        border-radius: 10px;
    }
    .comment{
        margin: 0 auto;
        width: 900px;
        height: 200px;
        padding: 10px;
        padding-top: 20px;
        border-top: 1px dashed rgba(108, 117, 125, 0.2);
    }

    .comment-form {
        width: 900px;
        margin: 0 auto;
        margin-bottom: 50px;
    }

    .comment-form .btn {
        width: 120px;
        height: 50px;
    }
    .comment-nickname{
        
        padding-top: 20px;
        padding-left: 10px;
        font-weight: bold;
        font-size: 20px;
        height: 80px;
    }
   .comment-idx {
    display: inline-block;
    margin-right: 10px;
    vertical-align: bottom;
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
        padding-left: 10px;
        color: #888;
        font-size: 14px;
        font-weight: lighter;
    }
    .emoticon{
        display: block;
        height: 30px;
    }
    .emoticon p{
        margin-bottom: 0;
        float: left;
        margin-right: 20px;
    }
    .user{
        margin-left: 20px;
        width: 400px;
    }
    .user p {
        margin-top: 10px;
        margin-bottom: 20px;
    }
    .symbols{
        font-size: 50px; 
        color: #999999;
    }
    .star {
        font-size: 65px;
    }
    .symbols:hover{
        color: black;
        cursor: pointer;
    }
    .turn-back{
        margin-left:30px; 
    }
    .symbol{
        position: relative;
    }
    .star{
        font-weight: 300;
        color: mediumturquoise;
        position: absolute; 
        right: 100px;
    }
    .heart{
        color: pink;
        top: 10px;
        right: 180px;
        position: absolute;
    }
    .airplane {
        position:absolute;
        right: 40px;
        top: 10px;
    }
    .category {
        font-weight: bold;
        font-size: 20px;
        margin-top: 10px;
        float: left;
        margin-bottom: 0;
        color: #888;
    }
    .category a:hover {
        cursor: pointer;
    }
    .fill-heart{
        font-variation-settings: 'FILL' 1
    }
    .fill-star {
        font-variation-settings: 'FILL' 1
    }
    .modal-button{
        width: 120px;
        height: 40px;
        margin: 0 10px;
    }
</style>