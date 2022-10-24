<template>
  <v-container fluid>
    <v-data-table
      :headers="headers"
      :items="userList"
      :search="search"
      no-data-text="Pas de données"
      no-results-text="Aucun résultat"
      :header-props="{
        sortByText: 'Trier par',
      }"
      :footer-props="{
        itemsPerPageText: 'Utilisateurs par page :',
        itemsPerPageAllText: 'Tous',
        pageText: '{0}-{1} de {2}',
        itemsPerPageOptions: [10, 20, 50, -1],
      }"
      sort-by="lastname"
      class="elevation-1"
    >
      <template #top>
        <!-- Top Bar -->
        <v-toolbar flat>
          <v-toolbar-title>Liste des utilisateurs</v-toolbar-title>
          <v-divider
            class="mx-4"
            inset
            vertical
          />
          <v-spacer />
          <v-col cols="6">
            <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Recherche"
              single-line
              hide-details
            />
          </v-col>
          <!-- Bouton new User -->
          <v-btn
            color="secondary primary--text"
            dark
            @click="openDialogCreate()"
          >
            Nouveau
          </v-btn>
        </v-toolbar>
      </template>

      <template #[`item.enabled`]="{ item }">
        <v-icon
          :color="getColor(item.enabled)"
          @click="openDialogEnabled(item)"
        >
          {{ getIcon(item.enabled) }}
        </v-icon>
      </template>

      <!-- part for edition and cancel print -->
      <template #[`item.actions`]="{ item }">
        <v-icon
          small
          class="mr-2"
          @click="openDialogEdit(item)"
        >
          mdi-pencil
        </v-icon>
        <v-icon
          small
          class="mr-2"
          @click="openDialogPassword(item)"
        >
          mdi-key
        </v-icon>
        <v-icon
          small
          class="mr-2"
          @click="openDialogDelete(item)"
        >
          mdi-delete
        </v-icon>
      </template>

      <!-- if need reset part -->
      <template #no-data>
        <v-btn
          color="secondary primary--text"
          @click="loadUserList()"
        >
          Recharger
        </v-btn>
      </template>
    </v-data-table>

    <!-- Dialog part -->
    <EditUserDialog
      v-model="dialogEdit"
      :user="editedItem"
    />

    <ConfirmDialog
      v-model="dialogDelete"
      text="Supprimer cet utilisateur ?"
      @confirm="doDeleteUser()"
    />

    <ConfirmDialog
      v-model="dialogEnabled"
      text="Modifier l'état de cet utilisateur ?"
      @confirm="doEnabledUser()"
    />

    <CreateUserDialog v-model="dialogCreate" />

    <ResetPasswordUserDialog
      v-model="dialogPassword"
      :user="editedItem"
    />
  </v-container>
</template>

<script>
import ConfirmDialog from '../../components/ConfirmDialog.vue';
import EditUserDialog from '../../components/user/EditUserDialog.vue';
import CreateUserDialog from '../../components/user/CreateUserDialog.vue';
import ResetPasswordUserDialog from '../../components/user/ResetPasswordUserDialog.vue';
import { mapActions, mapGetters } from 'vuex';

export default {
  name: 'UserList',

  components: {
    ConfirmDialog,
    EditUserDialog,
    CreateUserDialog,
    ResetPasswordUserDialog,
  },

  data () {
    return {
      dialogEdit: false,
      dialogDelete: false,
      dialogEnabled: false,
      dialogCreate: false,
      dialogPassword: false,

      search: '',
      headers: [
        { text: 'Prénom',  value: 'firstname' },
        { text: 'Nom',     value: 'lastname'  },
        { text: 'E-mail',  value: 'email'     },
        { text: 'Rôle',    value: 'role.displayName'      },
        { text: 'Statut',  value: 'enabled'   },
        { text: 'Actions', value: 'actions'   , sortable: false },
      ] ,

      editedItem: {},
    };
  },

  computed: {
    ...mapGetters('user', {
      userList: 'getUserList',
    }),
  },

  created () {
    this.loadUserList();
  },

  methods: {
    ...mapActions('user', {
      loadUserList: 'loadAllUserList',
      deleteUser: 'deleteUser',
      enabledUser: 'enabledUser',
    }),
    openDialogEdit(item) {
      this.editedItem = Object.assign({}, item);
      this.dialogEdit = true;
    },
    openDialogEnabled(item) {
      this.editedItem = Object.assign({}, item);
      this.dialogEnabled = true;
    },
    openDialogDelete(item) {
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },
    doDeleteUser() {
      this.deleteUser(this.editedItem);
    },
    doEnabledUser() {
      this.editedItem.enabled = !this.editedItem.enabled;
      this.enabledUser(this.editedItem);
    },
    openDialogCreate() {
      this.dialogCreate = true;
    },
    openDialogPassword(item) {
      this.editedItem = Object.assign({}, item);
      this.dialogPassword = true;
    },
    getColor(enabled) {
      return enabled ? 'green' : 'red';
    },
    getIcon(enabled) {
      return enabled ? 'mdi-check-bold' : 'mdi-close-thick';
    },
  },

};
</script>