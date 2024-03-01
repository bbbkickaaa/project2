<template>
    <div class ="container">
        <div class="board-table">
                <table class="table">
                    <thead>
                        <tr>
                            <th scope="col">글번호</th>
                            <th scope="col">제목</th>
                            <th scope="col">작성자</th>
                            <th scope="col">조회수</th>
                        </tr>
                    </thead>
                    <tbody v-if="limitedBoard.length > 0" >
                        <tr  v-for="list in limitedBoard" :key="list.boardId">
                            <td>{{ list.boardId }}</td>
                            <td><a class="title" @click="toDetails(list.boardId)">{{ list.title }} {{ '[' + list.commentCount + ']'}} <span v-if="list.likes">{{ '+' + list.likes }}</span></a></td>
                            <td>{{ list.nickname }}</td>
                            <td>{{ list.views }}</td>
                        </tr>
                        <!-- 추가 게시글들 -->
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
               <button @click="writePost" class="btn btn-secondary write-board">글쓰기</button>
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
data(){
    return{
        TheBoard :{data : []},
        GetPageInfo : [],
        currentPage:'',
        totalPages:''
    }
},
computed: {
    limitedBoard: function() {
        return this.TheBoard.data.slice(0, 20);
    },
    emptyRowsCount: function() {
      return Math.max(20 - this.limitedBoard.length, 0);
    },
    pageNumbers() {
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
}
}
,
mounted(){
  this.getBoard(0);
},
methods :{
    getBoard(page){
        this.$axios.get('/api/board/get-all',  {params: {page: page,size: 20}})
          .then(response => {
            this.TheBoard.data = response.data.content;
            this.totalPages = response.data.totalPages;
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
    },
    setPage(page){
        this.currentPage = page;
        this.getBoard(page);

    },
    
    writePost() {
        this.$router.push('/main/post')
    },
    toDetails(id){
        this.$router.push({ path: `/main/detail/${id}` });
    }
}
}
</script>

<style scoped>
.container {
    height: 1200px;
}
.board-table {
    margin: 0 auto;
    width: 1000px !important;
    height: auto;
    box-sizing: border-box;
    margin-bottom: 30px;
}
.table th, .table td {
    height: 41px;
    box-sizing: border-box;
}

.table th {
    text-align: center;
    border-top: 1px solid rgba(108, 117, 125, 0.5);
}

.table th:first-child, .table td:first-child{
    width: 80px;
    border-right: 1px dashed rgba(108, 117, 125, 0.5);
    text-align: center;
    border-left: 1px solid rgba(108, 117, 125, 0.5);
}

.table th:nth-child(2), .table td:nth-child(2){
    padding-left: 20px;
}

.table th:nth-child(3), .table td:nth-child(3){
    width: 150px;
    border-right: 1px dashed rgba(108, 117, 125, 0.5); 
    text-align: center;
    border-left: 1px dashed rgba(108, 117, 125, 0.5);
}
.table th:nth-child(4), .table td:nth-child(4){
    width: 100px;
    text-align: center;
    border-right: 1px solid rgba(108, 117, 125, 0.5);
}
.sections{
    width :1000px;
    display: relative;
    margin-bottom : 100px;
}
.sections .write-board {
    width: 80px;
    margin : 0 auto;
    position: absolute;
    right: 160px;
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
</style>
