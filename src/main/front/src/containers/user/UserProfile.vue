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
        <v-card class="px-4">
          <ValidationObserver
            v-slot="{ invalid, handleSubmit }"
            ref="observer"
          >
            <v-form @submit.prevent="handleSubmit(doChangePassword)">
              <v-card-title>Profil</v-card-title>
              <v-card-text>
                <v-row>
                  <v-col
                    cols="12"
                    sm="6"
                    md="6"
                  >
                    <v-text-field
                      v-model="user.firstname"
                      label="Prénom"
                      prepend-inner-icon="mdi-account"
                      disabled
                    />
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="6"
                  >
                    <v-text-field
                      v-model="user.lastname"
                      label="Nom"
                      prepend-inner-icon="mdi-account"
                      disabled
                    />
                  </v-col>
                </v-row>
                <v-text-field
                  v-model="user.email"
                  label="E-mail"
                  prepend-inner-icon="mdi-mail"
                  disabled
                />

                <ValidationProvider
                  v-slot="{ errors }"
                  name="Mot de passe"
                  rules="required|min:12"
                >
                  <v-text-field
                    v-model="user.password"
                    :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="showPassword ? 'text' : 'password'"
                    label="Mot de passe"
                    prepend-inner-icon="mdi-lock"
                    counter
                    :error-messages="errors[0]"
                    @click:append="showPassword = !showPassword"
                  />
                </ValidationProvider>

                <ValidationProvider
                  v-slot="{ errors }"
                  name="Confirmer le mot de passe"
                  rules="required|min:12"
                >
                  <v-text-field
                    v-model="user.verify"
                    block
                    :append-icon="showVerify ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="showVerify ? 'text' : 'password'"
                    label="Confirmer le mot de passe"
                    prepend-inner-icon="mdi-lock"
                    counter
                    :error-messages="errors[0]"
                    @click:append="showVerify = !showVerify"
                  />
                </ValidationProvider>
              </v-card-text>


              <v-divider light />
              <v-card-actions>
                <!-- Register button -->
                <v-btn
                  :disabled="invalid"
                  class="mr-4"
                  color="secondary primary--text"
                  type="submit"
                  x-large
                  block
                  right
                >
                  Modifier le mot de passe
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
import { mapActions, mapGetters } from 'vuex';
import { ValidationObserver, ValidationProvider } from 'vee-validate';

export default {
  name: 'UserProfile',

  components: {
    ValidationProvider,
    ValidationObserver,
  },

  data: () => ({
    dialog: true,
    valid: true,
    showPassword: false,
    showVerify: false,
  }),

  computed: {
    ...mapGetters('user', {
      user: 'getCurrentUser',
    }),
  },

  methods: {
    ...mapActions('user', {
      changePasswordUser: 'changePasswordUser',
    }),
    ...mapActions('popup', {
      showPopup: 'showPopup',
    }),

    doChangePassword() {
      this.changePasswordUser(this.user).then(() => {
        this.showPopup({
          text: 'Votre Mot de Passe a bien été modifié.',
          color: 'info',
        });
        this.user.password = '';
        this.user.verify = '';
        this.$nextTick(() => this.$refs.observer.reset());
      });
    },
  },
};
</script>