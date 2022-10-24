<template>
  <v-dialog
    v-model="dialog"
    width="800"
  >
    <v-card class="px-4">
      <ValidationObserver
        v-slot="{ invalid, handleSubmit }"
        ref="observer"
      >
        <v-form @submit.prevent="handleSubmit(doCreateUser)">
          <v-card-title>Ajout d'un utilisateur</v-card-title>
          <v-card-text>
            <v-row>
              <v-col
                cols="12"
                sm="6"
                md="6"
              >
                <ValidationProvider
                  v-slot="{ errors }"
                  name="Prénom"
                  rules="required"
                >
                  <v-text-field
                    v-model="firstname"
                    label="Prénom"
                    prepend-inner-icon="mdi-account"
                    :error-messages="errors[0]"
                  />
                </ValidationProvider>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="6"
              >
                <ValidationProvider
                  v-slot="{ errors }"
                  name="Nom"
                  rules="required"
                >
                  <v-text-field
                    v-model="lastname"
                    label="Nom"
                    prepend-inner-icon="mdi-account"
                    :error-messages="errors[0]"
                  />
                </ValidationProvider>
              </v-col>
            </v-row>

            <ValidationProvider
              v-slot="{ errors }"
              name="Rôle"
              rules="required"
            >
              <v-select
                v-model="role"
                :items="roleList"
                label="Rôle"
                item-text="displayName"
                return-object
                prepend-inner-icon="mdi-shield-lock"
                :error-messages="errors[0]"
              />
            </ValidationProvider>

            <ValidationProvider
              v-slot="{ errors }"
              name="E-mail"
              rules="required|email"
            >
              <v-text-field
                v-model="email"
                label="E-mail"
                prepend-inner-icon="mdi-mail"
                :error-messages="errors[0]"
              />
            </ValidationProvider>

            <ValidationProvider
              v-slot="{ errors }"
              name="Mot de passe"
              rules="required|min:12"
            >
              <v-text-field
                v-model="password"
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
                v-model="verify"
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
            <v-spacer />
            <v-btn
              color="primary"
              @click="onCancel()"
            >
              Annuler
            </v-btn>
            <v-btn
              :disabled="invalid"
              class="primary--text"
              color="secondary"
              type="submit"
            >
              Ajouter
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

import roles from '../../roles';

export default {
  name: 'CreateUserDialog',

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
  },

  data: () => ({
    dialog: false,

    showPassword: false,
    showVerify: false,

    roleList: [
      roles.SUPERADMIN,
      roles.SUPERVISOR,
      roles.USER,
    ],

    firstname: '',
    lastname: '',
    role: null,
    email: '',
    password: '',
    verify: '',
  }),

  watch: {
    dialog(value) {
        this.$emit('setVisible', value);
        if(value) {
          this.reset();
        }
    },
    visible(value) {
        this.dialog = value;
    },
  },

  methods: {
    ...mapActions('user', {
      register: 'register',
    }),

    reset() {
      this.showPassword = false;
      this.showVerify = false;

      this.firstname = '';
      this.lastname = '';
      this.role = null;
      this.email = '';
      this.password = '';
      this.verify = '';

      this.$nextTick(() => this.$refs.observer.reset());
    },

    doCreateUser() {
      this.register({
        firstname: this.firstname,
        lastname: this.lastname,
        role: this.role,
        email: this.email,
        password: this.password,
        verify: this.verify,
      }).then(() => {
        this.dialog = false;
      });
    },

    onCancel() {
      this.dialog = false;
    },
  },
};
</script>