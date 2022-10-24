<template>
  <v-dialog
    v-model="dialog"
    max-width="600"
  >
    <v-card class="px-4">
      <ValidationObserver
        v-slot="{ invalid, handleSubmit }"
        ref="observer"
      >
        <v-form @submit.prevent="handleSubmit(doChangePassword)">
          <v-card-title>Changer le mot de passe</v-card-title>
          <v-card-text>
            <v-container>
              <!-- Password -->
              <ValidationProvider
                v-slot="{ errors }"
                name="Confirmer le mot de passe"
                rules="required|min:12"
              >
                <v-text-field
                  v-model="editedUser.password"
                  :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="showPassword ? 'text' : 'password'"
                  label="Mot de passe"
                  prepend-inner-icon="mdi-lock"
                  counter
                  :error-messages="errors[0]"
                  @click:append="showPassword = !showPassword"
                />
              </ValidationProvider>

              <!-- Confirme password -->
              <ValidationProvider
                v-slot="{ errors }"
                name="Confirmer le mot de passe"
                rules="required|min:12"
              >
                <v-text-field
                  v-model="editedUser.verify"
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
            </v-container>
          </v-card-text>

          <!-- action part -->
          <v-divider light />
          <v-card-actions>
            <v-spacer />
            <!-- Cancel button -->
            <v-btn
              color="primary"
              @click="onCancel()"
            >
              Annuler
            </v-btn>
            <!-- Modify password button -->
            <v-btn
              :disabled="invalid"
              class="primary--text"
              color="secondary"
              type="submit"
            >
              Modifier
            </v-btn>
          </v-card-actions>
        </v-form>
      </ValidationObserver>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapActions } from 'vuex';
import { ValidationObserver, ValidationProvider } from 'vee-validate';

export default {
  name: 'ResetPasswordUserDialog',

  components: {
    ValidationProvider,
    ValidationObserver,
  },

  model: {
    prop: 'visible',
    event: 'setVisible',
  },

  props: {
    visible: { type: Boolean, default: false },
    user: { type: Object, default: null },
  },

  data: () => ({
    dialog: false,
    editedUser: {},

    showPassword: false,
    showVerify: false,
  }),

  watch: {
    dialog(value) {
      this.$emit('setVisible', value);
    },
    visible(value) {
      this.dialog = value;
    },
    user(value) {
      this.editedUser = value;
    },
  },

  methods: {
    ...mapActions('user', {
      changePasswordUser: 'changePasswordUser',
    }),

    doChangePassword() {
      this.changePasswordUser(this.editedUser).then(() => {
        this.dialog = false;
      });
    },

    onCancel() {
      this.dialog = false;
    },
  },

};
</script>

<style scoped>

</style>