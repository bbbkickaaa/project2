    <template>
    <div class="report-box2">
      <h4>신고게시판</h4>
      <form @submit.prevent="submitReport">
        <select v-model="report.reportType">
          <option value="report">신고</option>
          <option value="suggest">건의</option>
          <option value="bug">버그</option>
        </select>
  
        <textarea v-model="report.reportContent" maxlength="150" minlength="10" placeholder="내용을 입력해 주세요."></textarea>
        
        <button type="submit">제출</button>
    </form>
    <button @click="toReportList" style="margin-top: 10px; width: 100%; background-color: #ccc;" v-if="role=='ADMIN'">목록보기</button>
    </div>
  </template>
  
  <script>
  export default {
    props : ['role'],
    data() {
      return {
        report:{
            reportContent: '',
            reportType: 'report'
        }
        
      };
    },
    mounted(){
        console.log(this.role);
    },
    methods: {
      submitReport() {
        this.$axios.post('/api/report/submit',this.report,{withCredentials: true})
        .then((response)=>{alert(response.data); this.close()});
      },
      close(){
        this.$emit('close');
      },
      toReportList(){
          this.$emit('admin-list');
          this.close();
      },
    }
  }
  </script>
<style scoped>
.report-box2 {
  padding: 20px;
  margin: 20px;
  border-radius: 10px;
  width: 500px;
  min-height: 400px;
}

.report-box2 h3 {
  margin-top: 0;
}

.report-box2 form {
  display: flex;
  flex-direction: column;
}

.report-box2 select {
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.report-box2 textarea {
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  height: 200px;
}

.report-box2 button {
  padding: 10px;
  background-color: blue;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.report-box2 button:hover {
  background-color: darkblue;
}
</style>