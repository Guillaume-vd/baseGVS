<template>
  <v-container fluid>
    <v-progress-linear
      v-if="loading"
      indeterminate
      color="primary lighten-1"
    />
    <v-data-table
      :headers="headers"
      :items="formattedLogList"
      :search="search"
      sort-by="date"
      :sort-desc="true"
      no-data-text="Pas de données"
      no-results-text="Aucun résultat"
      :header-props="{
        sortByText: 'Trier par',
      }"
      :footer-props="{
        itemsPerPageText: 'Lignes par page :',
        itemsPerPageAllText: 'Tous',
        pageText: '{0}-{1} de {2}',
        itemsPerPageOptions: [20, 50, 100, -1],
      }"
      class="elevation-1"
    >
      <template #top>
        <v-toolbar flat>
          <v-toolbar-title>Log</v-toolbar-title>
          <v-divider
            class="mx-4"
            inset
            vertical
          />
          <v-spacer />
          <v-col
            cols="4"
            md="2"
          >
            <v-select
              v-model="selectLogSinceDay"
              :items="date"
              label="Date"
              single-line
              hide-details
            />
          </v-col>
          <v-col cols="6">
            <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Recherche"
              single-line
              hide-details
            />
          </v-col>
        </v-toolbar>
      </template>

      <template #[`item.comment`]="{ item }">
        <div style="white-space:pre-wrap; word-wrap:break-word;">
          {{ item.comment }}
        </div>
      </template>

      <template #no-data>
        <v-btn
          color="secondary primary--text"
          @click="reload()"
        >
          Recharger
        </v-btn>
      </template>
    </v-data-table>
    <v-progress-linear
      v-if="loading"
      indeterminate
      color="primary lighten-1"
    />
  </v-container>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

export default {
  name: 'Log',

  data () {
    return {
      formattedLogList: [],
      search: '',
      headers: [
        { text: 'Date'       , value: 'date'     , width: '15%', sort: (a, b) => a.value.getTime() - b.value.getTime() }, // cf comment before watch: { logList() }
        { text: 'Utilisateur', value: 'user'     , width: '10%'                  },
        { text: 'Action'     , value: 'action'   , width: '10%'                  },
        { text: 'User Agent' , value: 'userAgent', width: '20%'                  },
        { text: 'Ip publique', value: 'publicIp' , width: '10%'                  },
        { text: 'Commentaire', value: 'comment'  , width: '25%', sortable: false },
      ],

      date: [
        { text:'1 semaine', value: 7 },
        { text:'1 mois', value: 31 },
        { text:'3 mois', value: 62 },
        { text:'6 mois', value: 182 },
        { text:'1 an', value: 365 },
        { text:'tous', value: 0 },
      ],
      selectLogSinceDay: 7,

      loading: true,
    };
  },

  computed: {
    ...mapGetters('log', {
      logList: 'getLogList',
    }),
    ...mapGetters('user', {
      userList: 'getUserList',
    }),
  },

  watch: {
    // if item.date is a Date, sorting works but filtering dont
    // if item.date is a String, sorting doesnt work but filtering does
    // solution is having item.date an object, with item.date.value a Date, with a custom sort function to sort by item.date.value
    //          and overriding item.date.toString() for the item to be displayed correctly and for its display String to be correctly
    //          used for filtering
    logList(value) {
      this.formattedLogList = value.map(
        item => ({  id: item.id,
                    date: { value: new Date(item.date), toString() { return this.value.toLocaleString(); } }, // cf comment before watch: { logList() }
                    user: this.userInfos(item),
                    action: item.action,
                    comment: item.comment,
                    publicIp: item.publicIp,
                    userAgent: item.userAgent,
        }));
        this.loading = false;
    },

    selectLogSinceDay(value) {
      this.loading = true;
      this.loadLogListSince({ days: value }).catch(() => { this.loading = false; });
    },
  },

  created() {
    this.loadUserList();
    this.loadLogListSince({ days: this.selectLogSinceDay }).catch(() => { this.loading = false; });
  },

  methods: {
    ...mapActions('log', {
      loadLogListSince: 'loadLogListSince',
    }),
    ...mapActions('user', {
      loadUserList: 'loadAllUserList',
    }),
    userInfos({ user_id }) {
      const found = this.userList.find(el => el.id === user_id); 
      return found.firstname + ' ' + found.lastname;
    },
    reload() {
      this.loadUserList();
      this.loadLogListSince({ days: this.selectLogSinceDay }).catch(() => { this.loading = false; });
    },
  },
};
</script>