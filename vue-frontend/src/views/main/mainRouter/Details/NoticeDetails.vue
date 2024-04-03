<template>
    <div class="wrap">
        <div class="symbol">
            <span class="material-symbols-outlined symbols turn-back" @click="ToMain">undo</span>
            <span class="material-symbols-outlined symbols heart" v-if="!IsRecommended" @click="PostRecommend">favorite</span>
            <span class="material-symbols-outlined symbols heart fill-heart" v-if="IsRecommended" @click="PostRecommend">favorite</span>
            <span @click="URLModalOpen" class="material-symbols-outlined symbols airplane ">near_me</span>
        </div>
        <div class="container mt-4 content-wrap">
            <div class="post-detail inner-box">
                <div class ="post-title ">
                    <div class="user">
                        <img :src="NoticeInfo.picture" style="width: 60px; height: 60px; border-radius: 50%; border: 3px solid darkolivegreen;" alt="user-picture">
                        <p class="author">{{ NoticeInfo.nickname }} <span class="badge bg-secondary">{{ NoticeInfo.level }}</span></p>
                    </div>
                    <h3 class="titles">{{ NoticeInfo.title  }}</h3>
                    <div class="emoticon">
                        <p style="color: #999999;" class="count"><span style="vertical-align: bottom;" class="material-symbols-outlined">visibility</span> {{ NoticeInfo.views }}</p>
                    </div>
                    <p class="category">공지사항</p>
                 </div>
                <p class="post-content contents" v-html="NoticeInfo.content"></p>
            </div>
                
            <div class="actions">
                <button class="btn btn-secondary" v-if="CompareWithUsersAlter()" @click="AlterBoard(id)">수정</button>
                <button class="btn btn-secondary" v-if="CompareWithUsersDelete()" @click="ShowModal = true" @close="closeModal">삭제</button>
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
            ShowModalURL : false,
            formWriteDate : '',
            formContentDate : '',
            CommentForm:{
                noticeId : '',
                userIdx : '',
                comment : '',
                whereParam :'',
            },
            IsFavorite : null,
            IsRecommended : null,
            ShowModal: false,
            userIdx : '',
            id: null,
            NoticeInfo : {
                title :'',
                content:'',
                id:'',
                likes:'',
                nickname:'',
                userIdx : '',
                views : '',
                writeDate : '',
                picture:'',
                level:null,
                likesUser:{},
                favorite:null,
                role:'',
            }
        }
    },
mounted(){
    this.CommentForm.whereParam = this.$route.path;
    const id = parseInt(this.$route.params.noticeId, 10);
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
    this.$axios.get('/api/notice/get-detail', { params: { id: id }, withCredentials: true})
    .then(response => {
            this.NoticeInfo = response.data;
            this.CommentForm.noticeId = response.data.id;
            this.NoticeInfo.content = DOMPurify.sanitize(response.data.content);
            this.PlusViews();
            this.checkRecommend();
          })
          .catch(error => {
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert(`${error.message}`);
            }
            this.$router.push('/main')
        });
  },
  ToMain(){
    this.$router.push('/main');
  },
  CompareWithUsersAlter(){
    return this.userIdx == this.NoticeInfo.userIdx
  },
  CompareWithUsersDelete(){
    return this.userIdx == this.NoticeInfo.userIdx || this.role =="ADMIN";
  },
  PlusViews(){
    this.$axios.post('/api/notice/post-views', this.NoticeInfo.id)
  },
  DeleteBoard(){
    this.$axios.post('/api/notice/delete-notice', this.NoticeInfo.id)
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
    this.$axios.post('/api/notice/post-recommend', this.CommentForm)
    .then(()=>{this.IsRecommended = !this.IsRecommended;})
    .catch((response)=>{alert(response.response.data);})
  },
    AlterBoard(id) {
    this.$router.push(`/main/notice/details/${id}/edit`);
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
    this.IsRecommended = this.NoticeInfo.likesUser.some(userId => 
        userId === Number.parseInt(this.userIdx)
    )},
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
    .heart{
        color: pink;
        top: 10px;
        right: 100px;
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