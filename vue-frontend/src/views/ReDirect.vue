<template>
<div>
</div>
</template>
 
<script>
import router from "@/router";
import store from "@/store"; // Vuex 스토어 파일 경로 수정
 
export default {
  created() {
    const token = this.$route.query.token
    console.log('token',token)
    if (token) {
      store.commit('setToken', token);
      sessionStorage.setItem('accessToken', token);
      window.alert('로그인하였습니다');
      router.push({path:'/'})
    } else {
      window.alert('로그인에 실패하였습니다.')
      router.push({path:'/login'})
    }
  }
}
</script>
 
<style scoped>
 
</style>