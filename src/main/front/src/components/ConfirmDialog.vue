<template>
  <v-dialog
    v-model="dialog"
    max-width="600"
  >
    <v-card class="px-4">
      <v-card-title>
        {{ text }}
      </v-card-title>
      <v-divider light />
      <v-card-actions>
        <v-spacer />
        <!-- Cancel button -->
        <v-btn
          color="primary"
          @click="onCancel()"
        >
          {{ textCancel }}
        </v-btn>
        <!-- Confirm button -->
        <v-btn
          class="primary--text"
          color="secondary"
          @click="onConfirm()"
        >
          {{ textConfim }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'ConfirmDialog',

  model: {
    prop: 'visible',
    event: 'setVisible',
  },

  props: {
    visible: { type: Boolean, default: false },
    text: { type: String, default: 'Êtes-vous sûr ?' },
    textCancel: { type: String, default: 'Annuler' },
    textConfim: { type: String, default: 'Confirmer' },
  },

  data: () => ({
    dialog: false,
  }),

  watch: {
    dialog(value) {
      this.$emit('setVisible', value);
    },
    visible(value) {
      this.dialog = value;
    },
  },

  methods: {
    onConfirm() {
      this.dialog = false;
      this.$emit('confirm');
    },
    onCancel() {
      this.dialog = false;
      this.$emit('cancel');
    },
  },
};
</script>