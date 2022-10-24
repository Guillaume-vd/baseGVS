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
        <v-form @submit.prevent="handleSubmit(onConfirm)">
          <v-card-title>Modifier un utilisateur</v-card-title>

          <v-card-text>
            <v-container>
              <v-row>
                <v-col
                  cols="12"
                  sm="6"
                >
                  <v-text-field
                    v-model="editedUser.firstname"
                    label="Prénom"
                    prepend-inner-icon="mdi-account"
                  />
                </v-col>
                <v-col
                  cols="12"
                  sm="6"
                >
                  <v-text-field
                    v-model="editedUser.lastname"
                    label="Nom"
                    prepend-inner-icon="mdi-account"
                  />
                </v-col>
              </v-row>
              <v-select
                v-model="editedUser.role"
                :items="roleList"
                label="Rôle"
                item-text="displayName"
                return-object
                prepend-inner-icon="mdi-shield-lock"
              />
              <ValidationProvider
                v-slot="{ errors }"
                name="E-mail"
                rules="email"
              >
                <v-text-field
                  v-model="editedUser.email"
                  label="E-mail"
                  prepend-inner-icon="mdi-mail"
                  :error-messages="errors[0]"
                />
              </ValidationProvider>
            </v-container>
          </v-card-text>

          <v-divider light />
          <v-card-actions>
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
              Enregistrer
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
  name: 'EditUserDialog',

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

    roleList: [
      roles.SUPERADMIN,
      roles.SUPERVISOR,
      roles.USER,
    ],

    dialog: false,
    editedUser: {},
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
      editUser: 'editUser',
    }),

    onConfirm() {
      this.editUser(this.editedUser).then(() => {
        this.dialog = false;
      });
    },

    onCancel() {
      this.dialog = false;
    },
  },
};
</script>