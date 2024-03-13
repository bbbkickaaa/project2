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
                            <td v-if="list.category && categoryType===0"><a class="category" @click="toCategory1(list.category)" >{{ list.category.category1 }}</a></td>
                            <td v-if="list.category && categoryType===1"><a class="category" @click="toCategory2(list.category)" >{{ list.category.category2 }}</a></td>
                            <td v-if="list.category && categoryType===2"><a class="category" @click="toCategory3(list.category)" > {{ list.category.category3 }}</a></td>
                            <td v-if="list.category && categoryType===3"><a class="category" > {{ list.category.category3 }}</a></td>
                            <td>
                                <a class="title" @click="toDetails(list.boardId,list.category)">{{ list.title }} 
                                <span v-if="list.commentCount" style="font-size: 14px; font-weight: bold; color: darkgreen; margin-left: 5px;">{{ '[' + list.commentCount + ']'}}</span> 
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
                    <div class="bottom-button">
                        <form class = "search" @submit.prevent="searchContent">
                            <select class="search-select" v-model="searchData.selectedOption">
                                <option value="title">제목</option>
                                <option value="index">글 번호</option>
                                <option value="author">작성자</option>
                            </select>
                            <input v-if="searchData.selectedOption !== 'index'" type="text" placeholder="Search" v-model="searchData.content" minlength="2">
                            <input v-else type="number" :min="1" placeholder="Search" v-model="searchData.content" minlength="2" class="number-input">
                            <button class="btn btn-danger"><span class="material-symbols-outlined" style="vertical-align: bottom; font-size: 30px;">search</span></button>
                            <button v-if="isSearched" @click="resetSearch" style=" margin-left: 5px;" class="btn btn-secondary"><span class="material-symbols-outlined" style="vertical-align: bottom; font-size: 30px;">close</span></button>
                        </form>
                        <button @click="writePost" class="btn btn-success write-board">새 글 작성하기</button>
                    </div>
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
        categoryType : null,
        TheBoard :{data : []},
        GetPageInfo : [],
        currentPage:'',
        totalPages:'',
        searchData : {
            selectedOption : 'title',
            content : '',
        },
        
        isSearched : false,
        selectedData : {
            selectedOption : '',
            content : '',
        },
    }
},
props:['category1','category2','category3'],
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
    this.countQuery();
    this.setPage(0);
},
watch: {
    '$route'(newRoute, oldRoute) {
        if (newRoute && oldRoute && newRoute.path !== oldRoute.path) {
            this.countQuery();
            this.setPage(0);
        }
    },
}
,
methods :{
    getBoard(page) {
        let params = {
            page: page,
            size: 15
        };
        if (this.selectedData.selectedOption !== '' && this.selectedData.content !== '')
        {
            params.option = this.selectedData.selectedOption;
            params.content = this.searchData.content;
        }
        if (this.categoryType === 3) {
            params.category1 = this.category1;
            params.category2 = this.category2;
            params.category3 = this.category3;
        } else if (this.categoryType === 2) {
            params.category1 = this.category1;
            params.category2 = this.category2;
        } else if (this.categoryType === 1) {
            params.category1 = this.category1;
        }
        this.$axios.get('/api/board/get-all', { params: params })
          .then(response => {
            this.TheBoard.data = response.data.content;
            this.totalPages = response.data.totalPages;
            console.log("sdada");
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
    setPage(page){
        this.currentPage = page;
        this.getBoard(page);

    },
    
    writePost() {
        this.$router.push('/main/post')
    },
    toDetails(id,category){
        this.$router.push({ path:`/main/${category.category1}/${category.category2}/${category.category3}/detail/${id}`});
    },
    toCategory1(category){
        this.$router.push({path: `/main/${category.category1}`});
        this.countQuery();
        this.setPage(0);
    },
    toCategory2(category){
        this.$router.push({path: `/main/${category.category1}/${category.category2}`});
        this.countQuery();
        this.setPage(0);
    },
    toCategory3(category){
        this.$router.push({path: `/main/${category.category1}/${category.category2}/${category.category3}`});
        this.countQuery();
        this.setPage(0);
    },
    searchContent(){
        this.selectedData = this.searchData;
        this.isSearched = true;
        this.setPage(0)

    },
    resetSearch(){
        this.selectedData = {};
        this.isSearched = false;
        this.searchData = {
            selectedOption : 'title',
            content : '',
        }
        this.$router.push({path: '/main'});
        this.setPage(0);
    },

    countQuery(){
    if(this.category3){
        this.categoryType = 3;
    }
    else if(this.category2){
        this.categoryType = 2;
    }
    else if(this.category1){
        this.categoryType = 1;
    }
    else{
        this.categoryType = 0;
    }
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

.sections{
    width :1000px;
    margin-bottom : 100px;
    margin: 0 auto;
    height: 150px;
    text-align: right;
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

.search {
    float: left;
    
}
.search button{
    height: 50px;
    width: 60px;
    border-radius: 0 ;
    border-bottom-right-radius: 5px;
    border-top-right-radius: 5px;
    box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.3);
    
}
.bottom-button {
    margin-bottom: 50px;
}
.search input{
    margin-right: 5px;

    border: 1px solid rgba(108, 117, 125, 0.2);
    padding-left: 30px;
    width: 250px;
    height: 50px;
    box-sizing: border-box;
    vertical-align: middle;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
  

}
.sections .write-board {
    width: 160px;
    height: 50px;
    margin-left: 20px;
    display: inline-block;
}

.search-select {
    margin-right: 5px;
    vertical-align: bottom;
    padding: 8px 12px;
    border: 1px solid #ccc;
    border-radius: 4px;
    background-color: white;
    box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
    font-size: 16px;
    color: #333;
    height: 50px;
}

.search-select:focus {
    outline: none;
    border-color: #007bff;
}

.number-input::-webkit-inner-spin-button {
  appearance: none;
  -moz-appearance: none;
  -webkit-appearance: none;
}
</style>
