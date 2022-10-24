<template>
  <v-app>
    <NavBar class="hidden-sm-and-down" />
    <NavDrawer class="hidden-md-and-up" />

    <v-main>
      <router-view />
    </v-main>

    <Footer />
    <LoginDialog />
    <PopUp />
  </v-app>
</template>

<script>
import NavBar from '@/components/NavBar';
import NavDrawer from '@/components/NavDrawer';
import Footer from '@/components/Footer';
import LoginDialog from '@/components/LoginDialog';
import PopUp from '@/components/PopUp';

export default {
  name: 'App',

  components: {
    NavBar,
    NavDrawer,
    Footer,
    LoginDialog,
    PopUp,
  },

  data: () => ({
  }),

  computed: {
  },

  watch: {
  },

  methods: {
  },
};
</script>
