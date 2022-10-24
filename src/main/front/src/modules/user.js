import Vue from 'vue';

import roles from '../roles';

export default {
    namespaced: true,
    state: {
        currentUser: undefined,
        authenticated: false,
        timeout: false,
        userList: [],
    },

    mutations: {
        SET_CURRENT_USER(state, user) {
            if(user) {
                user.role = roles.parse(user.role);
            }
            state.currentUser = user;
        },
        SET_AUTHENTICATED(state, authenticated){
            state.authenticated = authenticated;
        },
        SET_TIMEOUT(state, timeout){
            state.timeout = timeout;
        },
        SET_USER_LIST(state, list) {

            for(let user of list) {
                user.role = roles.parse(user.role);
            }

            state.userList = list;
        },
        REMOVE_USER_FROM_LIST(state, id) {
            const i = state.userList.findIndex(el => el.id === id);
            state.userList.splice(i, 1);
        },
        UPDATE_USER_FROM_LIST(state, user) {
            const i = state.userList.findIndex(el => el.id === user.id);
            state.userList.splice(i, 1, user);
        },
        ADD_USER_TO_LIST(state, user) {
            state.userList.push(user);
        },
    },

    actions: {
        login({ commit }, { email, password }) {
            return Vue.axios.post('api/login', {
                email,
                password,
            }).then(response => {
                commit('SET_CURRENT_USER', response.data.user);
                commit('SET_AUTHENTICATED', response.data.authenticated);
            });
        },

        loadCurrentAuthenticated({ commit }) {
            return Vue.axios.get('api/user/current')
            .then(response => {
                commit('SET_CURRENT_USER', response.data.user);
                commit('SET_AUTHENTICATED', response.data.authenticated);
            });
        },

        loadCurrentAuthenticatedIfUndefined({ commit, state }) {
            if(state.currentUser === undefined)
            {
                return Vue.axios.get('api/user/current')
                .then(response => {
                    commit('SET_CURRENT_USER', response.data.user);
                    commit('SET_AUTHENTICATED', response.data.authenticated);
                });
            }

            return Promise.resolve();
        },

        logout({ commit }) {
            return Vue.axios.post('api/logout')
            .then(response => {
                commit('SET_AUTHENTICATED', response.data.authenticated);
            });
        },

        register({ commit }, { firstname, lastname, role, email, password, verify }){
            return Vue.axios.post('api/user', {
                email: email,
                role: role.name,
                firstname: firstname,
                lastname: lastname,
                matching_password: verify,
                password: password,
            }).then(response => {
                commit('ADD_USER_TO_LIST', { id: response.data.id, firstname, lastname, email, role, enabled: true });
            });
        },

        loadAllUserList({ commit }){
            return Vue.axios.get('/api/users')
            .then(response => {
                commit('SET_USER_LIST', response.data);
            });
        },

        editUser({ commit }, user){
            return Vue.axios.put(`/api/user/${user.id}`, {
                email: user.email,
                role: user.role?.name,
                firstname: user.firstname,
                lastname: user.lastname,
            }).then(() => {
                commit('UPDATE_USER_FROM_LIST', user);
            });
        },

        enabledUser({ commit }, user){
            return Vue.axios.put(`/api/user/${user.id}/enabled`, {
                enabled: user.enabled,
            }).then(() => {
                commit('UPDATE_USER_FROM_LIST', user);
            });
        },

        deleteUser({ commit }, { id }){
            return Vue.axios.delete(`/api/user/${id}`)
            .then(() => {
                commit('REMOVE_USER_FROM_LIST', id);
            });
        },

        changePasswordUser({ commit }, user){
            return Vue.axios.put(`/api/user/${user.id}/password`, {
                matching_password: user.verify,
                password: user.password,
            }).then(() => {
                commit('UPDATE_USER_FROM_LIST', user);
            });
        },
    },

    getters: {
        getCurrentUser(state) {
            return state.currentUser;
        },
        isAuthenticated(state) {
            return state.authenticated;
        },
        isTimeout(state) {
            return state.timeout;
        },
        isAllowedUSER(state) {
            return state.authenticated && state.currentUser.role.rank >= roles.USER.rank;
        },
        isAllowedSUPERVISOR(state) {
            return state.authenticated && state.currentUser.role.rank >= roles.SUPERVISOR.rank;
        },
        isAllowedSUPERADMIN(state) {
            return state.authenticated && state.currentUser.role.rank >= roles.SUPERADMIN.rank;
        },
        getUserList(state) {
            return state.userList;
        },
    },
};