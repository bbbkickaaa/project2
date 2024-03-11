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
                            <th scope="col">조회수</th>
                        </tr>
                    </thead>
                    <tbody v-if="limitedBoard.length > 0" >
                        <tr  v-for="list in limitedBoard" :key="list.boardId">
                            <td>{{ list.boardId }}</td>
                            <td><span v-if="list.category">{{ list.category.category1 }}</span></td>
                            <td>
                                <a class="title" @click="toDetails(list.boardId,list.category)">{{ list.title }} 
                                <span v-if="list.commentCount" style="font-size: 14px; font-weight: bold; color: dodgerblue; margin-left: 5px;">{{ '[' + list.commentCount + ']'}}</span> 
                                <span style="font-size: 14px; font-weight: bold; color: palevioletred; margin: 0 5px;" v-if="list.likes">{{ '+   ' + list.likes }}</span>
                                </a>
                            </td>
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
                <button @click="writePost" class="btn btn-primary write-board">새 글 작성하기</button>
                <nav aria-label="Page navigation" class="paging mt-4">
                    <ul class="pagination justify-content-center">
                        <li class="page-item" v-for="n in pageNumbers" :key="n">
                            <a class="page-link" @click="setPage(n-1)">{{ n }}</a>
                        </li>
                    </ul>
                </nav>
            </div>
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
        return this.TheBoard.data.slice(0, 15);
    },
    emptyRowsCount: function() {
      return Math.max(15 - this.limitedBoard.length, 0);
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
        this.$axios.get('/api/board/get-all',  {params: {page: page,size: 15}})
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
    toDetails(id,category){
        this.$router.push({ path:`/main/${category.category1}/${category.category2}/${category.category3}/detail/${id}`});
    }
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
  line-height: 50px;
  height: 50px !important;
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

.sections{
    width :1000px;
    margin-bottom : 100px;
    margin: 0 auto;
    height: 100px;
    text-align: right;
}
.sections .write-board {
    width: 160px;
    height: 50px;
    margin : 0 auto;
    display: inline-block;
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
