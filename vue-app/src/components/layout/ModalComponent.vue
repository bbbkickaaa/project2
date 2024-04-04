<template>
    <div v-if="show" class="modal-overlay">
      <div class="modal-content">
        <span class="close" @click="closeModal" >&times;</span>
        <slot></slot>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      show: {
        type: Boolean,
        default: false // 초기값을 false로 설정
      }
    },
    methods: {
      closeModal() {
        this.$emit('close');
      },
      watch: {
    
  } 
    }
  };
  </script>
  
  <style scoped>
  .modal-overlay {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.6);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
  }
  
  .modal-content {
    background-color: #ffffff;
    padding: 30px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    min-height: 500px;
    overflow-y: auto;
    width: 80%;
    max-width: 600px;
    z-index: 1001;
    position: relative;
  }
  
  .close {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 1.5rem;
    font-weight: bold;
    cursor: pointer;
  }
  
  .modal-content {
    transform: scale(0);
    animation: modal-show 0.3s forwards;
  }
  
  @keyframes modal-show {
    from {
      transform: scale(0);
      opacity: 0;
    }
    to {
      transform: scale(1);
      opacity: 1;
    }
  }
  
  .content-wrapper {
    max-height: 600px;
    overflow-y: scroll;
  }
  </style>