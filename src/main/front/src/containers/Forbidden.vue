<template>
  <v-container
    fill-height
    fluid
  >
    <v-row
      no-gutters
      align="center"
      justify="center"
    >
      <v-col
        cols="12"
        sm="8"
        md="4"
        lg="4"
      >
        <v-alert
          type="error"
        >
          Accès Interdit
        </v-alert>
      </v-col>
    </v-row>
  </v-container>
</template>


<script>
export default {
  name: 'Forbidden',

  components: {
  },

  data: () => ({
  }),

  computed: {
  },

  created() {
  },

  methods: {
  },
};
</script>
