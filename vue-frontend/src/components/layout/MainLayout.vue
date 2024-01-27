<template>
    <div>
      <AppHeader />
      <router-view/>
    </div>
  </template>
  
  <script>
  import AppHeader from './AppHeader.vue';
  
  export default {
    components: {
        AppHeader
    }
  };
  </script>