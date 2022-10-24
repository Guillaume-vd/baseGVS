<template>
  <v-app-bar
    v-if="isAuthenticated"
    color="primary"
    dark
    flat
    fixed
    app
  >
    <v-toolbar-title>
      <router-link :to="{ name: 'Home' }">
        <v-img
          src="../assets/logo_header_footer.png"
          alt="GVS"
          height="auto"
          width="247px"
        />
      </router-link>
    </v-toolbar-title>
    <v-toolbar-items>
      <v-btn
        class="mx-2 white--text"
        plain
        :to="{ name: 'Home' }"
      >
        dossiers en cours
      </v-btn>
      <v-btn
        v-if="isAllowedSUPERVISOR"
        class="mx-2 white--text"
        plain
        :to="{ name: 'Home' }"
      >
        archives
      </v-btn>
      <v-menu
        v-if="isAllowedSUPERADMIN"
        offset-y
      >
        <template #activator="{ on, attrs }">
          <v-btn
            class="mx-2 white--text"
            plain
            v-bind="attrs"
            v-on="on"
          >
            back office
          </v-btn>
        </template>
        <v-list>
          <v-list-item :to="{ name: 'BackofficeShowcase' }">
            <v-list-item-title>Vitrine</v-list-item-title>
          </v-list-item>
          <v-list-item :to="{ name: 'BackofficeOption' }">
            <v-list-item-title>Option</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
      <v-menu
        v-if="isAllowedSUPERADMIN"
        offset-y
      >
        <template #activator="{ on, attrs }">
          <v-btn
            class="mx-2 white--text"
            plain
            v-bind="attrs"
            v-on="on"
          >
            profils
          </v-btn>
        </template>
        <v-list>
          <v-list-item :to="{ name: 'Home' }">
            <v-list-item-title>Suivi commercial</v-list-item-title>
          </v-list-item>
          <v-list-item :to="{ name: 'UserList' }">
            <v-list-item-title>Liste des utilisateurs</v-list-item-title>
          </v-list-item>
          <v-list-item :to="{ name: 'Log' }">
            <v-list-item-title>Consulter les Logs</v-list-item-title>
          </v-list-item>
          <v-list-item :to="{ name: 'UserProfile' }">
            <v-list-item-title>Profil</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
      <v-btn
        v-if="!isAllowedSUPERADMIN && isAllowedSUPERVISOR"
        class="mx-5 white--text"
        plain
        :to="{ name: 'Home' }"
      >
        Suivi commercial
      </v-btn>
      <v-btn
        v-if="!isAllowedSUPERADMIN"
        class="mx-5 white--text"
        plain
        :to="{ name: 'UserProfile' }"
      >
        Profil
      </v-btn>
    </v-toolbar-items>

    <v-spacer />
    <v-divider vertical />

    <v-btn
      class="mx-5 white--text"
      plain
      @click="logout()"
    >
      déconnexion
    </v-btn>
  </v-app-bar>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

export default {
  name: 'NavBar',

  data: () => ({
  }),

  computed: {
    ...mapGetters('user', {
      isAuthenticated: 'isAuthenticated',
      isAllowedSUPERVISOR: 'isAllowedSUPERVISOR',
      isAllowedSUPERADMIN: 'isAllowedSUPERADMIN',
    }),
  },

  watch: {
  },

  created() {

  },

  methods: {
    ...mapActions('user', {
      sendLogout: 'logout',
    }),

    logout() {
      this.sendLogout()
          .then(() => {
            this.$router.push({ name: 'Login' });
          });
    },
  },
};
</script>