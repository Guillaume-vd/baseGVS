<template>
  <v-snackbar
    v-model="showPopup"
    class="mb-5"
    :timeout="5000"
    :color="color"
  >
    <v-icon
      class="pr-3"
      dark
      large
    >
      {{ color == 'error' ? 'mdi-alert-circle-outline' : 'mdi-checkbox-marked-circle-outline' }}
    </v-icon>

    {{ text }}

    <template #action="{ attrs }">
      <v-btn
        text
        v-bind="attrs"
        @click="showPopup = false"
      >
        Fermer
      </v-btn>
    </template>
  </v-snackbar>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex';

export default {
  name: 'PopUp',

  data: () => ({
    showPopup: false,
  }),

  computed: {
    ...mapGetters('popup', {
      text: 'getText',
      color: 'getColor',
      visible: 'isVisible',
    }),
  },

  watch: {
    showPopup(value) {
      if(!value) {
        this.setVisible(false);
      }
    },
    visible(value) {
      if(value) {
        this.showPopup = true;
      }
    },
  },

  methods: {
    ...mapMutations('popup', {
      setVisible: 'SET_VISIBLE',
    }),
  },
};
</script>