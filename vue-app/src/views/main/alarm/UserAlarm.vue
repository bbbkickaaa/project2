<template>
    <div class="alarm-wrap" >
        <ul class="alarm-list" ref="alarmList" v-if="alarms" @scroll="handleScroll" >
            <li v-for="alarm in alarms" :key="alarm.id">
                <div v-if="alarm.type==='COMMENT' && alarm.view===false" class="alarm-item not-read">
                    <span @click="deleteAlarm(alarm.id)" style="float: right;" class="material-symbols-outlined close">close</span>
                    <a><h5 class="alarm-title" @click="ToPage(alarm.url,alarm.id)">{{ alarm.content }}</h5></a>
                    <span class="alarm-time">{{ alarm.time }}</span>
                    <p class="alarm-message">{{ alarm.message }}</p>
                </div>
                <div v-else-if="alarm.type==='COMMENT' && alarm.view===true" class="alarm-item">    
                    <span @click="deleteAlarm(alarm.id)" style="float: right;" class="material-symbols-outlined close">close</span>
                    <a><h5 class="alarm-title" @click="ToPage(alarm.url,alarm.id)">{{ alarm.content }}</h5></a>
                    <span class="alarm-time">{{ alarm.time }}</span>
                    <p class="alarm-message">{{ alarm.message }}</p>
                </div>

                <div v-else-if="alarm.type==='MESSAGE' && alarm.view===false" class="alarm-item not-read">
                    <span @click="deleteAlarm(alarm.id)" style="float: right;" class="material-symbols-outlined close">close</span>
                    <a><h5 class="alarm-title" @click="ToMessage(alarm.id)">{{ alarm.content }}</h5></a>
                    <span class="alarm-time">{{ alarm.time }}</span>
                    <p class="alarm-message">{{ alarm.message }}</p>
                </div>

                <div v-else-if="alarm.type==='MESSAGE'&& alarm.view===true"  class="alarm-item">
                    <span @click="deleteAlarm(alarm.id)" style="float: right;" class="material-symbols-outlined close">close</span>
                    <a><h5 class="alarm-title" @click="ToMessage(alarm.id)">{{ alarm.content }}</h5></a>
                    <span class="alarm-time">{{ alarm.time }}</span>
                    <p class="alarm-message">{{ alarm.message }}</p>
                </div>
            </li>
        </ul>
        <ul v-else class="alarm-list" style="margin: 20px 0; text-align: center;">
            <li class="alarm-item">해당하는 데이터가 없습니다!</li>
        </ul>
    </div>
</template>
  
  <script>
  export default {
    data() {
        return {
            pages :0,
            alarms : []}
    },
    mounted() {
        this.getAlarm();
        this.addScrollListener();
    },
    unmounted() {
        this.removeScrollListener();
    },
    methods : {
        getAlarm(){
            this.$axios.get('/api/alarm/get-all',{params:{page :this.pages,size:7,sort:'id,desc'}, withCredentials: true})
            .then((response)=>{
                this.alarms = response.data;});
        },
        ToPage(url,id){
            this.ReadComment(id);
            this.$router.push(url);
            this.$emit('ToClose');
        },
        ToMessage(id){
            this.ReadComment(id);
            this.$emit('ToMessage',id);
        },
        ReadComment(id){
            this.$axios.put(`/api/alarm/read/${id}`)
        },
        deleteAlarm(id){
            this.$axios.delete(`/api/alarm/read/${id}`)
            .then(()=>{this.alarms = this.alarms.filter((s) => s.id !== id);})
        },
        loadMoreData() {
            this.pages++;
            this.$axios.get('/api/alarm/get-all', {
                params: {
                page: this.pages,
                size: 7,
                sort: 'id,desc'
                },
                withCredentials: true
            })
            .then(response => {
                this.alarms = [...this.alarms, ...response.data];
            })
            },
        addScrollListener() {
            const alarmListElement = this.$refs.alarmList;
            if (alarmListElement) {
            alarmListElement.addEventListener('scroll', this.handleScroll);
            }
        },
        removeScrollListener() {
            const alarmListElement = this.$refs.alarmList;
            if (alarmListElement) {
            alarmListElement.removeEventListener('scroll', this.handleScroll);
            }
        },
        handleScroll(e) {
            const { scrollHeight, scrollTop, clientHeight } = e.target;
            const isAtTheBottom = scrollHeight === scrollTop + clientHeight;
            // 일정 한도 밑으로 내려오면 함수 실행
            if (isAtTheBottom) {
                this.loadMoreData();}

            },
    }
  };
  </script>
  
  <style scoped>
  .modal-body {
    padding: 20px;
  }
  
  .alarm-list {
  list-style: none;
  padding: 0;
  overflow-y: auto;
  max-height: 500px; /* 또는 적절한 높이 값 */
}
  
  .alarm-item {
    background: #f9f9f9;
    padding: 15px;
    border-radius: 5px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    height: 90px;
    margin-bottom: 20px;
    opacity: 0.6;
    color: #333;
  }

  .not-read {
    opacity: 1;
    color: black;
    background-color: rgba(9, 212, 23, 0.4);
  }
  
  .alarm-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
  }
  
  .alarm-title {
    margin: 0;
    font-size: 16px;
    font-weight: bold;
  }
  
  .alarm-time {
    font-size: 14px;
    color: #666;
  }
  
  .alarm-message {
    margin: 0;
    color: #333;
  }
  
  .alarm-footer {
    margin-top: 10px;
    text-align: right;
  }
  
  .mark-read-btn {
    background-color: #4CAF50;
    color: white;
    border: none;
    padding: 5px 10px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 14px;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .mark-read-btn:hover {
    background-color: #45a049;
  }
  .alarm-wrap{
    height: 500px;
    margin-top: 40px;
  }
  .close:hover{
    cursor: pointer;
  }
  </style>
  