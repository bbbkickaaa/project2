<template>
    <div class="admin-container">
          <div class="card admin-card">
            <div class="admin-header">
              <h4>관리자 페이지</h4>
            </div>
            <div class="card-body">
              <div>
                <label for="suspensionDuration" class="form-label">정지 기간을 설정하세요.</label>
                <select v-model="suspensionDuration" class="form-select" id="suspensionDuration">
                    <option value="300">5분</option>
                    <option value="1800">30분</option>
                    <option value="3600">1시간</option>
                    <option value="21600">6시간</option>
                    <option value="43200">12시간</option>
                    <option value="86400">1일</option>
                    <option value="259200">3일</option>
                    <option value="604800">7일</option>
                    <option value="2592000">30일</option>
                    <option value="7776000">90일</option>
                    <option value="15552000">180일</option>
                    <option value="31104000">360일</option>
                    <option value="3153600000">평생</option>
                </select>
              </div>
              <button class="btn btn-primary" @click="applySuspension">적용</button>
            </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props:['id'],
    data() {
      return {
        suspensionDuration: '3600' // Default value
      };
    },
    methods: {
      applySuspension() {
        console.log(this.id);
        this.$axios.post('/api/admin/block-by-site',{id:this.id,duration:this.suspensionDuration})
        .then(()=>{alert("적용 되었습니다."); this.toMain()})
      },
      toMain(){
        this.$emit('close-modal')
      },
    }
  };
  </script>
  
  <style scoped>
    
    .card {
        width: 400px;
    }
    .admin-container {
      margin: auto;
      padding: auto 0;
    }
    .admin-card {
      box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
      transition: 0.3s;
    }
    .admin-card:hover {
      box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
    }
    .admin-header {
      background-color: #007bff;
      color: white;
      padding: 10px 15px;
    }
    .admin-header h4 {
      margin: 0;
    }
    .form-select {
      margin-bottom: 15px;
    }
  </style>
  