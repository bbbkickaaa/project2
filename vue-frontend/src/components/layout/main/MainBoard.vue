<template>
    <div id="app" class="container mt-4">
        <h2 class="mb-4">게시판 미리보기</h2>
        <div class="list-group">
            <a href="#" class="list-group-item list-group-item-action" aria-current="true" v-for="n in 20" :key="n">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">게시물 제목 {{ n }}</h5>
                    <small>3일 전</small>
                </div>
                <p class="mb-1">게시물 내용이 여기에 표시됩니다...</p>
                <small>작성자 이름</small>
            </a>
        </div>
        <nav aria-label="Page navigation" class="mt-4">
            <ul class="pagination justify-content-center">
                <li class="page-item" v-for="n in 5" :key="n">
                    <a class="page-link" href="#">{{ n }}</a>
                </li>
            </ul>
        </nav>
    </div>  
</template>


<script>
export default {}
</script>
