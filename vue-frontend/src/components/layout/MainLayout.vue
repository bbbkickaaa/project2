<template>
    <div class="all-wrap">
      <div class="all-bgc">
        <div class="app-logo" style="text-align: center; padding-top: 50px;">
          <a class="main-logo" @click="refreshView"><img src="/MainLogo.png" style="width:150px" alt="logo"></a>
        </div>
        <AppHeader @send-role="saveRole" @Alter-identity="handleAlterIdentity"></AppHeader>
        <AppCategory @category-selected="setCategory1"></AppCategory>
        <div class="wraps">
         <router-view :role="role" :key="refreshKey"></router-view>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import AppCategory from '@/views/main/AppCategory.vue';
  import AppHeader from '@/views/main/header/AppHeader.vue';
  export default {
    components: {
        AppHeader,
        AppCategory,
    },
    data() {
      return {
        role : '',
        refreshKey: 0,
        Category1 : '',
      }
    },
    methods:{
      handleAlterIdentity() {
        this.$router.push('/main/identity')
    },
    setCategory1(selectedCategory){
      this.Category1 = selectedCategory;
    },
    refreshView() {
      this.refreshKey++;
      this.$router.push('/main');
    },
    saveRole(role){
      this.role = role;
    },
  },
  };
  </script>
  <style >
    .wraps{
          position : relative;
          width: 1100px;
          margin: 0 auto;
  }
  html {
    background-color: #EEEEEE;
  }
  #app{
    background-color: #EEEEEE;
  }
  .all-wrap{
    width: 1400px;
    margin: 0 auto;
    margin-bottom: 100px;
    margin-top: 100px;
  }
  .all-bgc { 
    min-height: 1300px;
    width: 1300px;
    padding: 30px;
    background-color: white;
    box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    margin: 50px auto;
  }
  .main-logo:hover{
    cursor: pointer;
    opacity: 0.7;
  }
  a:hover {cursor: pointer;}
  </style>