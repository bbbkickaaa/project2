<template>
    <div class="container mt-4 wrap-category">
        <ul class="category1">
            <li 
                v-for="category in category1" 
                :key="category.value"
                :class="{ 'active-category': menuCategory1 === category.value }"
                @click="selectCategory(category.value)">
                {{ category.name }}
            </li>
        </ul>


        <ul class="category2" v-if="menuCategory1==='chat'">
            <li v-for="subcategory in subCategories.chat" :key="subcategory.value"
                :class="{ 'active-category2': selectedSubCategory === subcategory.value }"
                @click="selectCategory2(subcategory.value)">
                {{ subcategory.name }}
            </li>
        </ul>

        <ul class="category2" v-if="menuCategory1==='game'">
            <li v-for="subcategory in subCategories.game" :key="subcategory.value"
                :class="{ 'active-category2': selectedSubCategory === subcategory.value }"
                @click="selectCategory2(subcategory.value)">
                {{ subcategory.name }}
            </li>
        </ul>


        <ul class="category2" v-if="menuCategory1==='beauty'">
            <li v-for="subcategory in subCategories.beauty" :key="subcategory.value"
                :class="{ 'active-category2': selectedSubCategory === subcategory.value }"
                @click="selectCategory2(subcategory.value)">
                {{ subcategory.name }}
            </li>
        </ul>


        <ul class="category2" v-if="menuCategory1==='study'">
            <li v-for="subcategory in subCategories.study" :key="subcategory.value"
                :class="{ 'active-category2': selectedSubCategory === subcategory.value }"
                @click="selectCategory2(subcategory.value)">
                {{ subcategory.name }}
            </li>
        </ul>


        <ul class="category2" v-if="menuCategory1==='travel'">
            <li v-for="subcategory in subCategories.travel" :key="subcategory.value"
                :class="{ 'active-category2': selectedSubCategory === subcategory.value }"
                @click="selectCategory2(subcategory.value)">
                {{ subcategory.name }}
            </li>
        </ul>


    </div>
    
</template>

<script>
export default {
    data(){
        return{
            menuCategory1 : '',
            menuCategory2 : '',
            selectedSubCategory: '',

            category1: [
                { name: '잡담', value: 'chat' },
                { name: '게임', value: 'game' },
                { name: '뷰티', value: 'beauty' },
                { name: '공부', value: 'study' },
                { name: '여행', value: 'travel' }
            ],
            subCategories: {
                    chat: [
                        { name: '잡담', value: 'chat' },
                        { name: '일상', value: 'common' },
                        { name: '연예', value: 'star' },
                        { name: '사랑', value: 'love'},
                        { name: '음식', value: 'food'}
                    ],
                    game: [
                        { name: '리그오브레전드', value: 'lol' },
                        { name: '오버워치', value: 'overWatch' },
                        { name: '메이플스토리', value: 'mapleStory' },
                        { name: '발로란트', value: 'valorant'},
                        { name: '마비노기', value: 'mabinogi'},
                    ],
                    beauty: [
                        { name: '화장', value: 'makeup' },
                        { name: '패션', value: 'fashion' },
                        { name: '피부', value: 'skin' },
                        { name: '다이어트', value: 'diet'},
                        { name: '헤어스타일', value: 'hairstyle'},
                    ],
                    study: [
                        { name: '자격증', value: 'certification' },
                        { name: '수능', value: 'suneung' },
                        { name: '토익', value: 'toeic' },
                        { name: '취미', value: 'hobby'},
                        { name: '면접', value: 'interview'},
                    ],
                    travel: [
                        { name: '해외', value: 'oversea' },
                        { name: '국내', value: 'domestic' },
                        { name: '축제', value: 'festival' },
                        { name: '이벤트', value: 'event'},
                    ],
            }

        }
    },
    created(){
        this.menuCategory1 = this.$route.params.category1 || '';
        this.menuCategory2 = this.$route.params.category2 || '';
    },
    watch: {
        '$route'(to) {
            this.menuCategory1 = to.params.category1 || '';
            this.menuCategory2 = to.params.category2 || '';
        }
    },
    methods: {
        selectCategory(category) {
            this.menuCategory1 = category;
            this.$router.push( {path:`/main/${category}`});
        },
        selectCategory2(category){
            this.selectedSubCategory = category;
            this.$router.push( {path:`/main/${this.menuCategory1}/${category}`});
        },
    }
}
</script>

<style scoped>
.wrap-category{
    width: 700px;
    height: 140px;
    border: 1px solid rgba(108, 117, 125, 0.2);
    margin: 80px auto;
    padding: 10px;
    position: relative;
    box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
    border-radius: 10px;

.category1 {
    padding-left: 0;
    text-align: center;
    margin: 0 auto;
    margin-top: 20px;
    height: 50px;
}
}
.category1 li {
    color: #198754;
    border-right: 1px solid #198754;
    list-style: none;
    display: inline-block;
    padding: 0 30px;
    font-size: 20px;
    font-weight: bold;
}
.category1 li:first-child{
    border-left: 1px solid #198754;
}

.category1 li:hover {
    cursor: pointer;
}
.category2{
    margin: 0 auto;
    margin-top: 10px;
    padding-left: 0;
    width: 650px;
    text-align: center;
}
.category2 li {
    color: #6c757d;
    list-style: none;
    display: inline-block;
    font-size: 16px;
    width: 130px;
    font-weight: 600;
}
.category2 li:hover {
    cursor: pointer;
    
}
.active-category {
  position: relative;
  display: inline-block;
}

.active-category::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 50%;
  transform: translateX(-50%);
  width: 60%;
  border-bottom: 1px solid #198754;
}

.active-category2 {
  position: relative;
  display: inline-block;
}

.active-category2::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 50%;
  transform: translateX(-50%);
  width: 60%;
  border-bottom: 1px solid #6c757d;
}
</style>

