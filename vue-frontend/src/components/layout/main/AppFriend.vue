<template>
    <div class="mt-2 friend-form">
      <div class="d-flex justify-content-center mb-3 buttons">
        <div class="button-style find-friend" :class="{ 'active-success': showFriendsList }" @click="clickFriend">
            친구 목록 보기
        </div>
        <div class="button-style find-block" :class="{ 'active-danger': showBlockedList }" @click="clickBlock">
            차단 목록 보기
        </div>
      </div>
  
        <div v-if="showBlockedList" class="alert alert-danger">
          <ul class="list-group" v-for="user in TheBoard.data" :key="user.id">
              <li class="list-group-item" ><img style="width: 20px; border-radius: 50%; margin-right: 10px; margin-left: 10px;"  :src="user.picture" alt="">
                {{ user.nickname }}<span class="badge bg-secondary" style="font-size: 10px;vertical-align: middle; margin-bottom: 3px; margin-left: 3px;">{{ user.userLevel.level }}</span>
                <span @click="deleteBlock(user.id)" class="material-symbols-outlined close">close</span> </li>
            </ul>
            <ul class="list-group" v-for="list in emptyRowsCount" :key="list">
              <li class="list-group-item" ></li>
            </ul>
        </div>
  
        <div v-if="showFriendsList" class="alert alert-success">
            <ul class="list-group" v-for="user in TheBoard.data" :key="user.id">
              <li class="list-group-item" ><img style="width: 20px; border-radius: 50%; margin-right: 10px; margin-left: 10px;"  :src="user.picture" alt="">
                <a @click="SendMessage(user.id)">{{ user.nickname }}</a><span class="badge bg-secondary" style="font-size: 10px;vertical-align: middle; margin-bottom: 3px; margin-left: 3px;">{{ user.userLevel.level }}</span>
                <span @click="deleteFriend(user.id)" class="material-symbols-outlined close">close</span> </li>
            </ul>
            <ul class="list-group" v-for="list in emptyRowsCount" :key="list">
              <li class="list-group-item" ></li>
            </ul>
        </div>

        <nav aria-label="Page navigation" class="paging mt-4">
            <ul class="pagination justify-content-center">
                    <li class="page-item" v-for="n in pageNumbers" :key="n">
                        <a class="page-link" @click="setPage(n-1)">{{ n }}</a>
                    </li>
            </ul>
        </nav>
    </div>
  </template>
  
  <script>
export default {
    data() {
      return {
        
        TheBoard:{data :[]},
        currentPage:'',
        totalPages:'',
        showBlockedList: false,
        showFriendsList: true,
      };
    },
mounted() {
    this.clickFriend();
},
computed: {
  emptyRowsCount() {
            return Math.max(0, 7 - this.TheBoard.data.length);
        },

    pageNumbers() {
        if (this.totalPages <= 1) {
        return [1];}
        const windowSize = 5;
        let startPage, endPage;

        if (this.totalPages <= windowSize) {
        startPage = 1;
        endPage = this.totalPages;
        } else {
        if (this.currentPage >= Math.ceil(windowSize / 2)) {
            startPage = Math.max(1, this.currentPage - Math.floor(windowSize / 2));
            endPage = startPage + windowSize - 1;
            if (endPage > this.totalPages) {
            endPage = this.totalPages;
            startPage = Math.max(1, endPage - windowSize + 1);
            }
        } else {
            startPage = 1;
            endPage = windowSize;
        }
        }
        const pages = [];
        for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
        }
        return pages;
    },
},
methods : {
    setPage(page){
        this.currentPage = page;
        if(this.showFriendsList){
          this.getFriend(page);
        }
        if(this.showBlockedList){
          this.getBlock(page);
        }
    },
    getFriend(page) {
        let params = {
            page: page,
            size: 7
        };
        this.$axios.get('/api/member/get-friend', { params: params })
          .then(response => {
            this.TheBoard.data = response.data.content;
            this.totalPages = response.data.totalPages;
            console.log(this.TheBoard.data.length);
          })
          .catch(error => {
            // 오류 처리
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert(`${error.message}`);
            }
          });
    },
    getBlock(page){
        let params = {
            page: page,
            size: 7
        };
        this.$axios.get('/api/member/get-block', { params: params })
          .then(response => {
            this.TheBoard.data = response.data.content;
            this.totalPages = response.data.totalPages;
            console.log(response.data)
          })
          .catch(error => {
            // 오류 처리
            if (error.response) {
              alert("Error: 오류가 발생했습니다.");
            } else if (error.request) {
              alert("Error: 서버로부터 응답이 없습니다.");
            } else {
              alert(`${error.message}`);
            }
          });
    },
    clickFriend(){
        this.getFriend(0);
        this.showBlockedList = false; 
        this.showFriendsList = true;

    },
    clickBlock(){
        this.getBlock(0);
        this.showFriendsList = false;
        this.showBlockedList = true; 
    },
    deleteFriend(id) {
      this.$axios.delete(`/api/member/delete-friend/${id}`, {
        withCredentials: true
    })
    .then(() => {
        this.TheBoard.data = this.TheBoard.data.filter(user => user.id !== id);
    });},
    deleteBlock(id) {
      this.$axios.delete(`/api/member/delete-block/${id}`, {
        withCredentials: true
    })
    .then(() => {
        this.TheBoard.data = this.TheBoard.data.filter(user => user.id !== id);
    });
  },
  SendMessage(id){
    this.$emit('showMessage',id)
  },
}
}

 </script>

  <style scoped>
 .friend-form {
    margin: 0 auto;
    width:500px;
 }
.list-group-item{
  width: 400px;
  height: 40px;
  margin: 0 auto;
}
.list-group {
  margin: 10px 0 ;
}

.close{
  font-size: 20px;
  color: firebrick;
  float: right;
}
.close:hover {
  cursor: pointer;
}

.button-style {
    padding: 10px 20px;
    color: green;
    background-color: white;
    border: 2px solid green;
    cursor: pointer;
    display: inline-block;
    margin-right: 10px;
    transition: background-color 0.3s, color 0.3s;
}

.button-style.active-success {
    color: white;
    background-color: green;
}

.button-style.find-block {
    color: red;
    border-color: red;
}

.button-style.active-danger {
    color: white;
    background-color: red;
}

.button-style:hover {
    opacity: 0.9; /* 호버 효과 */
}
  </style>