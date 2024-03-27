<template>
    <div style="border: 1px solid rgba(224, 224, 224, 0.3);">
        <div>
            <div class = "user-img" ><img style="width: 80px; height: 80px; border-radius: 50%;" :src="userInfo.picture" alt="">
                <h4>{{ userInfo.nickname }}</h4>
            </div>
            <p style="text-align: right; padding-top: 10px; padding-right: 10px;"> 가입일 : {{ userInfo.createdDate }}</p>
        </div>
        <div class="recent-content">
            <h5>최근 작성글</h5>
            <ul class="recent-write" v-for="list in userInfo.dto" :key="list.id">
                <a @click="toDetail(list.id,list.category1,list.category2,list.category3)"><li>{{ formatTitle(list.title) }}<span>작성일 : {{ list.date }}</span><span  style="margin-right: 20px;" >조회수 : {{ list.view }}</span></li></a>
            </ul>
        </div>
        <div class="bottom-button">
            <button class="btn btn-success" @click="toMessage">쪽지 보내기</button>
            <button class="btn btn-primary" @click="addFriend">친구 추가</button>
            <button class="btn btn-danger" @click="addBlock">차단 하기</button>
            <button class="btn btn-secondary" @click="addBlockByAdmin" v-if="role === 'ADMIN'">계정 정지</button>
        </div>
    </div>
</template>
<script>
export default{
props : ['id','role']
,data(){
    return {
        userInfo : [],
    }},
methods :{
    getUser(){
    this.$axios.get('/api/member/get-another-user',{params:{id:this.id}})
    .then((response)=>{this.userInfo = response.data; console.log(this.userInfo.dto);})
 },
  toDetail(id, category1, category2, category3) {
    this.$router.push(`/main/${category1}/${category2}/${category3}/detail/${id}`);
},
  formatTitle(title) {
    return title.length > 12 ? title.substring(0, 12) + '...' : title;
  },
  toMessage(){
    this.$emit('switchModal','message')
  },
  addFriend(){
    this.$axios.post('/api/member/add-friend-user', this.id)
    .then(response=>{alert(response.data)}).catch(error=>{alert(error.response.data)})
  },
  addBlock(){
    this.$axios.post('/api/member/block-user', this.id)
    .then(response=>{alert(response.data)}).catch(error=>{alert(error.response.data)})
  },
  addBlockByAdmin(){
    this.$emit('switchModal' , 'admin')
  },
},
mounted(){
    this.getUser();
},
}
</script>
<style scoped>
.user-img{
    width: auto;
    height: 150px;
    background-color: rgba(224, 224, 224, 0.3);
    text-align: center;
    line-height: 100px;
}
.user-img h4{ 
    padding-top: 10px;
}
.recent-content {
    border-top: 1px solid rgba(224, 224, 224, 0.6);
    border-bottom: 1px solid rgba(224, 224, 224, 0.6);
    padding-top: 20px;
    padding-bottom: 20px;
    height: 250px;
    width: 90%;
    margin: 0 auto;
    margin-bottom: 20px;
}
.recent-write {
 margin-left: 10px;
 margin-bottom: 0px;
}
.bottom-button {
    height: 70px;
    text-align: center;

}
.bottom-button button {
    margin: 0 10px;
    margin-bottom: 20px;
    width: 150px;
    height: 50px;
    vertical-align: bottom;
}
.recent-write li {
    color: #999999;
    list-style:none;
    padding: 8px 0;
    font-size: 16px;
    position: relative;

}
.recent-write li  span{
    font-size: 10px;
    position:absolute;
    right: 0;
    bottom: 10px;
}
.recent-write li span:first-child{
    right: 90px;
}   
.recent-write:hover{
    text-decoration: underline;
}
</style>