import Vue from 'vue';
import VueRouter from 'vue-router';

import roles from './roles';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Login',
    meta: { role: roles.NONE },
    component: () => import('./containers/Login.vue'),
  },
  {
    path: '/home',
    name: 'Home',
    meta: { role: roles.USER },
    component: () => import('./containers/Home.vue'),
  },
  {
    path: '/forbidden',
    name: 'Forbidden',
    meta: { role: roles.NONE },
    component: () => import('./containers/Forbidden.vue'),
  },
  {
    path: '/user/list',
    name: 'UserList',
    meta: { role: roles.SUPERADMIN },
    component: () => import('./containers/user/UserList.vue'),
  },
  {
    path: '/log',
    name: 'Log',
    meta: { role: roles.SUPERADMIN },
    component: () => import('./containers/Log.vue'),
  },
  {
    path: '/user/profile',
    name: 'UserProfile',
    meta: { role: roles.USER },
    component: () => import('./containers/user/UserProfile.vue'),
  },
  {
    path: '/backoffice/showcase',
    name: 'BackofficeShowcase',
    meta: { role: roles.SUPERADMIN },
    component: () => import('./containers/backoffice/Showcase.vue'),
  },
  {
    path: '/backoffice/option',
    name: 'BackofficeOption',
    meta: { role: roles.SUPERADMIN },
    component: () => import('./containers/backoffice/Option.vue'),
  },
];

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes,
});

// navigation guard //
import store from './store';

router.beforeEach((to, from, next) => {
  return store.dispatch('user/loadCurrentAuthenticatedIfUndefined').then(() => 
  {
    const isAuthenticated = store.getters['user/isAuthenticated'];
    const user = store.getters['user/getCurrentUser'];

    if (to.name !== 'Login' && !isAuthenticated)    { next({ name: 'Login'      }); return; } // if not authenticated, redirect to Login
    if (to.name === 'Login' &&  isAuthenticated)    { next({ name: 'Home'       }); return; } // if already authenticated, redirect to Home
    if (to.name == null)                            { next({ name: 'Home'       }); return; } // if unknown route, redirect to Home
    if (user && to.meta.role.rank > user.role.rank) { next({ name: 'Forbidden'  }); return; } // if not enough privilege, redirect to Forbidden
 
    next();
  });
});

export default router;
