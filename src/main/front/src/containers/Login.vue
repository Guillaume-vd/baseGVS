<template>
  <v-container
    fill-height
    fluid
  >
    <v-row
      align="center"
      justify="center"
      no-gutters
    >
      <v-col
        cols="10"
        sm="8"
        md="4"
      >
        <v-card class="pa-5">
          <v-img
            alt="Logo GVS"
            contain
            height="150"
            src="../assets/pingouins.png"
          />
          <v-card-title class="primary--text text-h4 justify-center">
            Empereur
          </v-card-title>
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
      </v-col>
    </v-row>
  </v-container>
</template>


<script>
import { ValidationObserver, ValidationProvider } from 'vee-validate';
import { mapActions } from 'vuex';

export default {
  name: 'Login',

  components: {
    ValidationProvider,
    ValidationObserver,
  },

  data: () => ({
    show: false,

    password: '',
    email: '',
  }),

  methods: {
    ...mapActions('user', {
      login: 'login',
    }),

    submit() {
      this.login({
        email: this.email,
        password: this.password,
      }).then(() => {
        this.$router.push({ name: 'Home' });
      });
    },
  },
};
</script>