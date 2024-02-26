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