<template>
    <div class ="container">
        <div class="board-table">
                <table class="table">
                    <thead>
                        <tr>
                            <th scope="col">글번호</th>
                            <th scope="col">카테고리</th>
                            <th scope="col">제목</th>
                            <th scope="col">작성자</th>
                        </tr>
                    </thead>
                    <tbody v-if="limitedBoard.length > 0" >
                        <tr  v-for="list in limitedBoard" :key="list.boardId">
                            <td>{{ list.boardId }}</td>
                            <td>{{ list.type }}</td>
                            <td>
                                <a class="title" @click="toReportDetails(list.boardId)">{{ subStringContent(list.content)}}</a>
                            </td>
                            <td>
                                <a @click="clickUserInfo(list.userId,list.nickname)" @close = "showUserInfo = false">{{ list.nickname }}</a>
                            </td>
                        </tr>
                    </tbody>
                    <tbody>
                        <!-- 게시글 데이터 반복 -->
                        <tr v-for="list in emptyRowsCount" :key="list">
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <!-- 추가 게시글들 -->
                    </tbody>
                </table>
            </div> 
                <div class ="sections">
                    <nav aria-label="Page navigation" class="paging mt-4">
                        <ul class="pagination justify-content-center">
                            <li class="page-item" v-for="n in pageNumbers" :key="n">
                                <a class="page-link" @click="handleRouteChange(n-1)">{{ n }}</a>
                            </li>
                        </ul>
                    </nav>
            </div>
            <modal-component :show="showUserInfo"  @close="showUserInfo = false,recentModal='userInfo'">
                <user-form  :role="role" v-if="recentModal === 'userInfo'" :id="clickedUserIdx" @close-modal="showUserInfo = false" @switch-modal="handleSwitchModal"></user-form>
                <user-message @close-modal="showUserInfo = false" :receiveId="clickedUserIdx" :nickname="clickedNickname" v-if="recentModal === 'message'" @switch-modal="handleSwitchModal" ></user-message>
                <admin-block :id="clickedUserIdx" v-if="recentModal === 'admin'" @close-modal="showUserInfo = false"></admin-block>
            </modal-component>
            <modal-component :show="showReportModal" @close="showReportModal = false" >
                <ReportView :id="clickedBoardId"></ReportView>
            </modal-component>
            
        </div>
</template>


<script>
import ModalComponent from '@/components/layout/ModalComponent.vue';
import UserForm from '@/views/main/UserInfo.vue';
import UserMessage from '@/views/main/alarm/UserMessage.vue';
import AdminBlock from '@/views/main/admin/AdminBlock.vue';
import ReportView from '@/views/main/modal/ReportView.vue'
export default {
components : {
    ModalComponent,
    UserForm,
    UserMessage,
    AdminBlock,
    ReportView
},

data(){
    return{
        showReportModal : false,
        clickedBoardId : null,
        recentModal : 'userInfo',
        showUserInfo : false,
        clickedUserIdx : null,
        clickedNickname : '',
        categoryType : null,
        TheBoard :{data : []},
        currentPage:'',
        totalPages:'',
        }
},
computed: {
    limitedBoard: function() {
        // this.TheBoard.data가 배열인지 확인하고, 그렇지 않으면 빈 배열을 사용
        if (Array.isArray(this.TheBoard.data)) {
            return this.TheBoard.data.slice(0, 15);
        }
        return [];
    },
    emptyRowsCount: function() {
      return Math.max(15 - this.limitedBoard.length, 0);
    },
    pageNumbers() {
        if (this.totalPages <= 1) {
        return [1];
    }
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

mounted(){
    this.handleRouteChange(0);
},

watch: {
    '$route'(newRoute, oldRoute) {
      if (newRoute && oldRoute && newRoute.path !== oldRoute.path) {
        this.handleRouteChange();
      }
    },
  },
methods :{
    handleRouteChange(page){
        this.isReport = true;
        this.setPageReport(page);
    },
    getReport(page){

    let params = {
            page: page,
            size: 15
        };
        this.$axios.get('/api/board/report', { params: params,withCredentials: true })
          .then(response => {
            
            this.TheBoard.data = response.data.content;
            this.totalPages = response.data.totalPages;
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
    setPageReport(page){
        this.currentPage = page;
        this.getReport(page);
    },
    clickUserInfo(userIdx,nickname){
        this.showUserInfo= true;
        this.clickedUserIdx=userIdx;
        this.clickedNickname = nickname;
    },
    handleSwitchModal(newModal) {
      this.recentModal = newModal;
    },
    subStringContent(content){
        if(content){
        return content.substr(0,30) + "  ..."}
        else{return content}
    },
    toReportDetails(id){
        this.clickedBoardId = id;
        this.showReportModal = true;
    },

}
}
</script>

<style scoped>
.container {
    height: 1100px;
    
}
.board-table {
    margin: 0 auto;
    width: 1000px !important;
    height: auto;
    box-sizing: border-box;
    margin-bottom: 30px;
    border-radius: 10px;
    box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
}

.table thead tr:first-child th:first-child {
  border-top-left-radius: 10px;
}

.table thead tr:first-child th:last-child {
  border-top-right-radius: 10px;
}

.table tbody tr:last-child td:first-child {
  border-bottom-left-radius: 10px;
}

.table tbody tr:last-child td:last-child {
  border-bottom-right-radius: 10px;
}

.table th, .table td {
  width: auto;
  vertical-align: middle;
  box-sizing: border-box;
  height: 55px !important;
  padding: 2px;
  text-align: left;
  box-sizing: border-box;
}

.table th {
  background-color: #f4f4f4;

}
.table th {
    border-top: 1px solid rgba(108, 117, 125, 0.2);
    text-align: center;
}

.table th:first-child, .table td:first-child{
    width: 100px;
    text-align: center;
    border-left: 1px solid rgba(108, 117, 125, 0.2);
}

.table th:nth-child(2), .table td:nth-child(2){
    width: 140px;
    text-align: center;
}

.table th:nth-child(3), .table td:nth-child(3){
    padding-left: 20px;
}
.table th:nth-child(4), .table td:nth-child(4){
    width: 120px;
    text-align: center;
}
.table th:nth-child(5), .table td:nth-child(5){
    width: 100px;
    text-align: center;
}

.title{
    color: black;
    text-decoration-line: none;
}
.title:hover {
    cursor: pointer;
    text-decoration-line: underline;
}
.page-link:hover {
    cursor: pointer;
}
.page-link{
    color: #888;
}

.category:hover{
    cursor: pointer;
    text-decoration: underline;
}
.category{
    color: black;
    text-decoration: none;
}

.number-input::-webkit-inner-spin-button {
  appearance: none;
  -moz-appearance: none;
  -webkit-appearance: none;
}
</style>
