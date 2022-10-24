import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        themes: {
          light: {
            primary: {
              base: 0x3b3835,
              lighten1: 0xa8a198,
            },
            secondary: {
              base: 0xffcf21,
              lighten1: 0xe6dcca,
            },
            info: 0x21759b,
          },
        },
      },
});
