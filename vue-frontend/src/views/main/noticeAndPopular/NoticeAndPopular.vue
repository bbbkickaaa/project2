<template>
  <div class="container mt-3">
    <!-- 버튼 그룹 -->
    <div class="btn-group" role="group" style="padding-left: 20px;">
      <button type="button" class="notice-btn" :class="{ 'selected': showSection === 'notices' }" @click="showSection = 'notices'"><span class="material-symbols-outlined" style="vertical-align: middle;">campaign</span>
          공지사항</button>
      <button type="button" class="notice-btn" :class="{ 'selected': showSection === 'popular' }" @click="showSection = 'popular'"><span class="material-symbols-outlined" style="vertical-align: middle;">flare</span>
          금주 전체 인기글</button>
    </div>

    <!-- 공지사항 섹션 -->
    <div v-if="showSection === 'notices'" class="mt-3 the-list">
      <h4 style=""><label style="font-weight: bold; float: left; margin-left: 10px; line-height: 30px; font-size: 40px;">|</label>    <span style="padding-left: 20px;">공지사항</span></h4>
      <ul class="list-group">
        <li class="list-group-item" v-for="(notice,index) in notices" :key="notice.id"><span style="margin-right: 10px;" class="badge bg-danger">{{index + 1}}</span> {{ notice.title }}</li>
      </ul>
    </div>

    <!-- 인기글 섹션 -->
    <div v-if="showSection === 'popular'" class="mt-3 the-list">
      <h4 style=""><label style="font-weight: bold; float: left; margin-left: 10px; line-height: 30px; font-size: 40px;">|</label>    <span style="padding-left: 20px;">금주 인기글</span></h4>
      <ul class="list-group">
        <li class="list-group-item" @click="toPath(post.category.category1,post.category.category2,post.category.category3,post.boardId)" v-for="(post,index) in popularPosts" :key="post.id">
        <span style="margin-right: 10px;" class="badge bg-success">{{index + 1}}</span>  {{ post.title }}
        <span v-if="post.commentCount > 0">{{ + post.commentCount }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showSection: 'notices', // 초기에 보여줄 섹션
      notices: [
        { id: 1, title: "공지사항 1" },
        { id: 2, title: "공지사항 2" },
        { id: 3, title: "공지사항 3" },
        { id: 4, title: "공지사항 4" },
        { id: 5, title: "공지사항 5" }
      ],
      popularPosts: []
    };
  },
  methods : {
    getPopular(){
      this.$axios.get('/api/board/get-popular')
      .then((response)=>{this.popularPosts = response.data.content; console.log(this.popularPosts)})
    },
    toPath(category1,category2,category3,id){
      console.log(`/${category1}/${category2}/${category3}/detail/${id}`);
      this.$router.push({path:`/main/${category1}/${category2}/${category3}/detail/${id}`})
    },

  },
  mounted(){
    this.getPopular();

  }
};
</script>

<style scoped>
    .container {
      width: 1050px;
      margin: 0 auto; 
      margin-bottom: 70px;
      box-sizing: border-box;

  }
  .the-list{
    width: 1000px;
    box-sizing: border-box;
    border-radius: 10px;
    box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
    padding: 20px;
    margin: 0 auto; 
  }
    

    
    h4 {
      color: #198754;
      margin-bottom: 15px; 
    }
    
    .list-group-item {
      border: none; 
      padding: 10px 50px;
      transition: background-color 0.3s ease; 
    }
    
    .list-group-item:hover {
      background-color: #f8f9fa; 
    }

    .notice-btn {
      border: none;
      background-color: white;
      margin-right: 10px;
      padding-bottom: 5px;

    }

    .selected{
      border-bottom: 3px solid #198754;
    }
    .list-group-item:hover{
      cursor: pointer;
      text-decoration: underline;
    }
    </style>
    