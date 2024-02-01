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
  /* 모달 오버레이 스타일 */
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
  
  /* 모달 컨텐츠 스타일 */
  .modal-content {
    background-color: #ffffff;
    padding: 30px; /* 컨텐츠 패딩 */
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    min-height: 500px; /* 최소 세로 높이를 800px로 설정 */
    overflow-y: auto; /* 세로 스크롤 추가 */
    width: 80%;
    max-width: 600px; /* 최대 너비 설정 */
    z-index: 1001;
    position: relative;
  }
  
  /* 모달 닫기 버튼 스타일 */
  .close {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 1.5rem;
    font-weight: bold;
    cursor: pointer;
  }
  
  /* 모달 애니메이션 스타일 */
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
  
  /* 모달 내부 컨텐츠 스타일 */
  .content-wrapper {
    max-height: 600px; /* 내부 텍스트 높이 제한 */
    overflow-y: scroll; /* 세로 스크롤 추가 */
  }
  </style>