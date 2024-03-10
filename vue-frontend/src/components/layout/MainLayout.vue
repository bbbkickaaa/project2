<template>
    <div class="all-wrap">
      <div class="all-bgc">
        <AppHeader @Alter-identity="handleAlterIdentity"></AppHeader>
        <div class="wraps">
          <router-view></router-view>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import AppHeader from './main/AppHeader.vue';
  export default {
    components: {
        AppHeader,
    },
    data() {
      return {
      }
    },
    methods:{
      handleAlterIdentity() {
        this.$router.push('main/identity')
    }
  }
  };
  </script>
  <style>
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
  </style>