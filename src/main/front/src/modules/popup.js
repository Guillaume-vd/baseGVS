export default {
    namespaced: true,

    state: {
        text: null,
        color: null,
        visible: false,
    },

    mutations: {
        SET_TEXT(state, text){
            state.text = text;
        },
        SET_COLOR(state, color){
            state.color = color;
        },
        SET_VISIBLE(state, visible){
            state.visible = visible;
        },
    },

    actions: {
        showPopup({ commit }, { text, color }) {
            return Promise.resolve()
            .then(() => {
                commit('SET_TEXT', text);
                commit('SET_COLOR', color);
                commit('SET_VISIBLE', true);
            });
        },
    },

    getters: {
        getText(state) {
            return state.text;
        },
        getColor(state) {
            return state.color;
        },
        isVisible(state) {
            return state.visible;
        },
    },
};