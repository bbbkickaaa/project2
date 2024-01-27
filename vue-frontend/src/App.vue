<template>
  <div id="app">
    <div id="content" class="content">
      <router-view></router-view>
    </div>
  </div>
  </template>

<script>

export default
{
  name : 'App',
};
</script>

<style>
body {
  margin: 0 auto !important;
  width: 1300px;
  height: 800px;
}

</style>