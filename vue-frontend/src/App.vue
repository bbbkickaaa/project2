<template>
  <div id="app">
    <AppHeader />
    <div id="content" class="content">
      <router-view></router-view>
    </div>
  </div>
  </template>

<script>
import AppHeader from './components/layout/AppHeader'

export default
{
  name : 'App',
  components:{
    AppHeader
  }
};
</script>