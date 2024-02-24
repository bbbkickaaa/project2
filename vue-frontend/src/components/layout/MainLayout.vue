<template>
    <div>
      <AppHeader></AppHeader>
        <div class="Wraps">
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
  };
  </script>
  <style>
    .Wraps{
          position : relative;
          width: 1300px;
          height: 1000px;
  }
  </style>