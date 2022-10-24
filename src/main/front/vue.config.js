
let proxyTomcat = {
  target: 'http://localhost:8080',
  secure: false,
};

module.exports = {
  devServer: {
    port: 8081,
    proxy: {
      '/api/*': proxyTomcat,
    },
  },
  transpileDependencies: [
    'vuetify',
  ],
  chainWebpack: config => {
    config.module
      .rule('vue')
      .use('vue-loader')
      .tap(options => {
          options.compiler = require('vue-template-babel-compiler');
          return options;
      });
  },
};
