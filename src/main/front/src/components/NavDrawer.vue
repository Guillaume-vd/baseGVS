<template>
  <div>
    <v-app-bar
      v-if="isAuthenticated"
      color="primary"
      dark
      flat
      fixed
      app
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-toolbar-title>
        <v-img
          src="../assets/logo_header_footer.png"
          alt="GVS"
          height="auto"
          width="247px"
        />
      </v-toolbar-title>
    </v-app-bar>

    <v-navigation-drawer
      v-model="drawer"
      color="primary"
      temporary
      app
    >
      <v-list
        nav
        dense
      >
        <v-list-item-group
          v-model="group"
          active-class="deep-yellow--text text--accent-4"
        >
          <router-link :to="{ name: 'Home' }">
            <v-img
              src="../assets/logo_header_footer.png"
              alt="GVS"
              height="auto"
              width="247px"
            />
          </router-link>
          <v-btn
            class="mx-5 my-1 mt-3 white--text"
            plain
            :to="{ name: 'Home' }"
          >
            dossiers en cours
          </v-btn>
          <v-btn
            v-if="isAllowedSUPERVISOR"
            class="mx-5 my-1 white--text"
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
                class="mx-5 my-1 white--text"
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
                class="mx-5 my-1 white--text"
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
            class="mx-5 my-1 white--text"
            plain
            :to="{ name: 'Home' }"
          >
            Suivi commercial
          </v-btn>
          <v-btn
            v-if="!isAllowedSUPERADMIN"
            class="mx-5 my-1 white--text"
            plain
            :to="{ name: 'UserProfile' }"
          >
            Profil
          </v-btn>
          <v-divider class="my-3" />
          <v-btn
            class="mx-5 white--text"
            plain
            bottom
            @click="logout()"
          >
            déconnexion
          </v-btn>
        </v-list-item-group>
      </v-list>
    </v-navigation-drawer>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

export default {
  name: 'NavDrawer',

  data: () => ({
    drawer: false,
    group: null,
  }),

  computed: {
    ...mapGetters('user', {
      isAuthenticated: 'isAuthenticated',
      isAllowedSUPERVISOR: 'isAllowedSUPERVISOR',
      isAllowedSUPERADMIN: 'isAllowedSUPERADMIN',
    }),
  },

  watch: {
    group () {
      this.drawer = false;
    },
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