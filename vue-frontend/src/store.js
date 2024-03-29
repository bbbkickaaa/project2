import Vue from 'vue'
import Vuex from 'vuex'
 
Vue.use(Vuex)
 
const store = new Vuex.Store({
    state: {
       token: null
    },
    mutations: {
        setToken(state, token) {
            state.token = token;
        }
    },
    methods: {
        clearState(state) {
            state.token = null;      
    }
}
})
 
export default store;