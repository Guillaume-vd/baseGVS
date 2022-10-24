import Vue from 'vue';
import './plugins/axios';
import App from './App.vue';
import vuetify from './plugins/vuetify';
import store from './store';
import router from './router';

Vue.config.productionTip = false;

new Vue({
  vuetify,
  store,
  router,
  render: h => h(App),
}).$mount('#app');


import { extend } from 'vee-validate';
import { required, min, email } from 'vee-validate/dist/rules';

extend('required', {
  ...required,
  message: 'Le champ {_field_} ne doit pas être vide',
});

extend('min', {
  ...min,
  message: 'Le champ {_field_} doit contenir au moins {length} caractères',
});

extend('email', {
  ...email,
  message: 'Le champ {_field_} doit être une adresse email valide',
});