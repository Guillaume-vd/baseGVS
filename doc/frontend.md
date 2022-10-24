# Frontend

Le frontend de l'application est réalisé en JavaScript avec le Framework Vue.js.

Les sources se trouvent dans le dossier [src/main/front](../src/main/front).

## Main 

### Plugins
L'application utilise certains plugin en plus du Framework Vue.js de base :
- [Vuetify 2](https://vuetifyjs.com/en/) : Une librairie UI contenant tous les éléments graphiques de base. 
  Il est initialisé dans le fichier [vuetify.js](../src/main/front/src/plugins/vuetify.js).
  Les thèmes de l'application (couleurs `primary`, `secondary`, ...) sont définis ici.
- [axios](https://axios-http.com/) : Un client Http permettant de faire les appels API.
  Initialisé dans [axios.js](../src/main/front/src/plugins/axios.js).
- [Vuex](https://vuex.vuejs.org/fr/) : Un gestionnaire d'état.
  Initialisé dans [store.js](../src/main/front/src/store.js).
  Permet de stocker des variables globales représentant l'état de l'application.
  Ses modules sont initialisés dans le dossier [modules](../src/main/front/src/modules). (Ne pas oublier d'ajouter les nouveaux modules dans [index.js](../src/main/front/src/modules/index.js))
- [Vee-Validate 3](https://vee-validate.logaretm.com/v3) : Utilitaire d'aide à la validation.
  Initialisé directement dans [main.js](../src/main/front/src/main.js).

### routes
Le fichier [router.js](../src/main/front/src/router.js) permet de définir les règles de routage de l'application.

Le routage s'effectue comme ceci:
```js
{
    path: '/home',
    name: 'Home',
    meta: { role: roles.USER },
    component: () => import('.path/Home.vue'),
},
```
- le champ `path` représente la valeur contenue dans l'URL
- le champ `name` permet d'identifier la route
- le champ `meta` permet de stocker des informations arbitraires sur la route ; il est utilisé pour la vérification du rôle de l'utilisateur
- le champ `component` permet de définir la ressource liée à la route

Le routeur contient un fonction "navigation guard" afin de vérifier si l'utilisateur est 
connecté et d'effectuer une redirection vers la page de connexion en cas de besoin.

### Vérification
Le fichier [main.js](../src/main/front/src/main.js) contient l'initialisation des méthodes de validation.
Ces méthodes sont héritées des méthodes par défaut de `Vee-Validate`, mais ont un message personnalisé pour être en Français.
```js
extend('required', {
  ...required,
  message: 'Le champ {_field_} ne doit pas être vide',
});
```

## App.vue
Le fichier [App.vue](../src/main/front/src/App.vue) est le corps de l'application. C'est lui qui est chargé dans le fichier [src/main.js](../src/main/front/src/main.js).

### Nav Bar
C'est dans le fichier [NavBar.vue](../src/main/front/src/components/NavBar.vue) que nous avons la barre de navigation.

Exemple d'un bouton :
```html
<v-btn
    v-if="isRole"
    class="mx-5 my-1 white--text"
    plain
    to="path"
>
    name
</v-btn>
```

### Les pages
Les pages chargées par le routeur sont automatiquement insérés dans [App.vue](../src/main/front/src/App.vue) à la place de la balise `<router-view />`

```html
<v-main>
    <router-view />
</v-main>
```

## Containers

Un `Container` représente une page complète. 

### La partie vue
La partie vue qui représente l'affichage est réalisée à l'aide des composants `Vuetify`
https://vuetifyjs.com/en/components/ entre les balises `<template>`. 

### La partie script
Dans cette partie nous déclarons
- Les `components` utilisé majoritairement pour la validation des champs requis
- Les `models` utilisé  
- Les `props` 
- Les `data` dans laquelle nous renseignons les données utilisés dans la partie vue. 
- Les `watch` utilisé pour la gestion des événements liés au pop-up
- Les `methods` utilisées pour faire appel au js ou effectuer un traitement sur les données.

### Le css
Il est possible de rajouter une troisième partie non obligatoire afin d'ajouter du code 
css dans l'application. Qui s'écrit de la manière suivante:
```html
<style lang="scss" scoped>
  .styleName {
    padding: 0;
    margin-bottom: 10px;
  }
</style>
```

## Components
Un component est très similaire aux containers, la seule différence est dans son but : un Container représente une page alors qu'un Component représente un composant. 
Un Component permet de fragmenter l'implémentation de l'application et d'être réutilisé à plusieurs endroits.