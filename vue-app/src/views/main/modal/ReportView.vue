<template>
    <div class="report-box">
      <h4>신고게시판</h4>
      <p class="user-id"><span>유저번호 : </span>{{ report.userId }}</p>
      <p class="user-nickname"><span> 유저 닉네임 : </span>{{report.nickname}}</p>
      <p class="report-type"><span> 신고 타입 : </span>{{report.type}}</p>
      <p class="write-date"><span> 작성일 : </span>{{ report.writeDay }}</p>
  
        <div class="text-form"><span>{{ report.content }}</span> </div>
    </div>
        
  </template>
  
  <script>
  export default {
    props : ['role','id'],
    data() {
      return {
        report:{
            boardId:'',
            userId : '',
            writeDay:'',
            content: '',
            type: '',
            nickname : '',
        }
        
      };
    },
    mounted(){
        this.getReport(this.id);
    },
    methods: {
      getReport(id) {
        this.$axios.get('/api/report/get-report',{params :{id:id}})
        .then((response)=>{this.report = response.data;})
      },
    }
  }
  </script>
<style>
.report-box {
  background-color: #fff; /* 배경색 */
  padding: 20px;
  margin: 20px auto; /* 중앙 정렬 */
  border: 1px solid #ddd; /* 테두리 */
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.2); /* 그림자 효과 */
  width: 500px;
  min-height: 400px;
  box-sizing: border-box; /* 패딩을 너비에 포함 */
}

.report-box h4 {
  margin-top: 0;
  color: #4a4a4a;
  border-bottom: 2px solid #6c757d;
  padding-bottom: 10px;
  font-size: 24px;
  text-align: center;
}

.report-box p {
  color: #333;
  line-height: 1.6;
  margin: 15px 0;
  font-size: 16px;
}

.report-box p span.label {
  font-weight: bold;
  color: #6c757d;
  margin-right: 10px;
}

.text-form {
  background-color: #f8f8f8;
  border: 1px solid #eaeaea;
  border-radius: 5px;
  padding: 15px;
  width: 100%;
  height: 200px;
  margin-top: 20px;
  overflow-y: auto;
  font-size: 14px;
  color: #333;
}



</style>