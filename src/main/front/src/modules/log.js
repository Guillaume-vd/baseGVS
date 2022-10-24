import Vue from 'vue';

export default {
    namespaced: true,

    state: {
        authenticated: false,
        logList: [],
    },

    mutations: {
        SET_AUTHENTICATED(state, authenticated){
            state.authenticated = authenticated;
        },
        SET_LOG_LIST(state, list) {
            state.logList = list;
        },
    },
    
    actions: {
        loadLogListSince({ commit }, { days }){
            return Vue.axios.get(`/api/logs?since=${days}`)
            .then(response => {
                commit('SET_LOG_LIST', response.data);
            });
        },
    },

    getters: {
        isAuthenticated(state) {
            return state.authenticated;
        },
        getLogList(state) {
            return state.logList;
        },
    },
};