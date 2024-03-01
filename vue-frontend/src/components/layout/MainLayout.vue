<template>
    <div>
      <AppHeader @Alter-identity="handleAlterIdentity"></AppHeader>
        <div class="wraps">
           <router-view></router-view>
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
          width: 1300px;
          height: 1000px;
  }
  </style>