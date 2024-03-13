// src/router/guards.js
import axios from 'axios';

export async function checkPostOwner(to, from, next) {
  try {
    const id = to.params.id;
    const accessToken = sessionStorage.getItem('accessToken');
    const response = await axios.get(`http://localhost:8080/api/member/check-post-owner/${id}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`
        }
      });
    if (response.status === 200) {
      next();
    } else {
      next('/main');
    }
  } catch (error) {
    console.error(error);
    next('/main');
  }
}
const categories = {
  category1: ['chat', 'game', 'beauty', 'study', 'travel'],
  category2: ['chat', 'lol', 'overWatch', 'mapleStory', 'valorant', 'mabinogi', 'makeup', 'fashion', 'skin', 'diet', 'hairstyle', 'certification', 'suneung', 'toeic', 'hobby', 'interview', 'oversea', 'domestic', 'festival', 'event'],
  category3: ['ask', 'info', 'free']
};

export async function validateCategoryRoute(to, from, next) {
  try {
    const { category1, category2, category3} = to.params;

    const validCategory1 = categories.category1.includes(category1);
    const validCategory2 = !category2 || categories.category2.includes(category2); // 카테고리 2가 없거나 유효한 경우를 고려
    const validCategory3 = !category3 || categories.category3.includes(category3); // 카테고리 3이 없거나 유효한 경우를 고려

    if (validCategory1 && validCategory2 && validCategory3) {
      next();
    } else {
      next('/main'); // 유효하지 않은 경우 현재 경로 유지
    }
  } catch (error) {
    console.error('라우터 가드에서 오류 발생:', error);
    next('/error'); // 서버 오류 시 오류 페이지로 이동
  }
}


