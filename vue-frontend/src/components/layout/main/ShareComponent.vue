<template>
    <div class="copy-wrap">

        <div class="copy-logo">
            
        </div>
        <div class="copy-url">
            <input :value="route"  disabled type="text">
            <button class="btn btn-success" @click="copyToClipboard">URL 복사</button>
        </div>
    </div>
    
</template>
<script>
export default{
    data(){
        return {
            route : '',
        }
    },
    mounted(){
        this.whatRoute();

    },
    methods : {
        whatRoute(){
            this.route = window.location.href;
        },
        async copyToClipboard() {
            try {
                const text = this.route;
                await navigator.clipboard.writeText(text);
                console.log("클립보드에 복사되었습니다.");
            } catch (err) {
                console.error("복사 실패:", err);
            }
            }
    },

}
</script>
<style>
.copy-wrap {
    width: 400px;
    height: 400px;
    margin: 0 auto;
}
.copy-logo {
    height: 300px;
}
.copy-url {}
.copy-url input {
    width: 300px;
}
.copy-url button {

}

</style>