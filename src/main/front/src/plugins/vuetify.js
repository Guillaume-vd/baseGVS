import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        themes: {
          light: {
            primary: {
              base: 0x000576,
              lighten1: 0xffb800,
            },
            secondary: {
              base: 0xffb800,
              lighten1: 0xffb800,
            },
            info: 0x21759b,
          },
        },
      },
});
