<template>
  <v-dialog
    v-model="dialog"
    persistent
    :max-width="width"
  >
    <v-card class="pa-5">
      <v-img
        alt="Logo OCF"
        contain
        height="150"
        src="../assets/pingouins.png"
      />
      <v-card-title class="primary--text text-h4 justify-center">
        Empereur
      </v-card-title>
      <v-alert type="error">
        Vous avez été déconnecté
      </v-alert>
      <ValidationObserver v-slot="{ invalid, handleSubmit }">
        <v-form @submit.prevent="handleSubmit(submit)">
          <v-card-text>
            <ValidationProvider
              v-slot="{ errors }"
              name="E-mail"
              rules="required|email"
            >
              <v-text-field
                v-model="email"
                label="E-mail"
                name="email"
                prepend-inner-icon="mdi-mail"
                :error-messages="errors[0]"
              />
            </ValidationProvider>

            <ValidationProvider
              v-slot="{ errors }"
              name="Mot de passe"
              rules="required"
            >
              <v-text-field
                v-model="password"
                :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
                :type="show ? 'text' : 'password'"
                label="Mot de passe"
                prepend-inner-icon="mdi-lock"
                :error-messages="errors[0]"
                @click:append="show = !show"
              />
            </ValidationProvider>
          </v-card-text>

          <v-divider light />

          <v-card-actions>
            <v-btn
              :disabled="invalid"
              color="primary"
              type="submit"
              text
              x-large
              block
              right
            >
              Connexion
            </v-btn>
          </v-card-actions>
        </v-form>
      </ValidationObserver>
    </v-card>
  </v-dialog>
</template>

<script>
import { ValidationObserver, ValidationProvider } from 'vee-validate';
import { mapActions, mapMutations, mapGetters } from 'vuex';

export default {
  name: 'LoginDialog',

  components: {
    ValidationProvider,
    ValidationObserver,
  },

  data: () => ({
    dialog: false,

    show: false,
    showlog: false,

    password: '',
    email: '',
  }),

  computed: {
    ...mapGetters('user', {
      isTimeout: 'isTimeout',
    }),

    width() {
        switch(this.$vuetify.breakpoint.name) {
            case 'xl': return '33%';
            case 'lg': return '33%';
            case 'md': return '33%'; // 4 cols
            case 'sm': return '66%'; // 8 cols
            case 'xs': return '83%'; // 10 cols
        }
        return '33%';
    },
  },

  watch: {
    isTimeout(value) {
      this.dialog = value;
    },
  },

  methods: {
    ...mapActions('user', {
      login: 'login',
    }),
    ...mapMutations('user', {
      setTimeout: 'SET_TIMEOUT',
    }),

    submit() {
      this.login({
        email: this.email,
        password: this.password,
      }).then(() => {
        this.setTimeout(false);
      });
    },
  },
};
</script>