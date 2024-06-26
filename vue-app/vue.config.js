const { defineConfig } = require('@vue/cli-service');
const path = require('path'); // Add this line to import the path module

module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: "../src/main/resources/static",
  indexPath: "index.html",
  devServer: {
    port: 3000,
    proxy: "http://localhost:3000"
  },
  chainWebpack: config => {
    const svgRule = config.module.rule("svg");
    svgRule.uses.clear();
    svgRule.use("vue-svg-loader").loader("vue-svg-loader");
  },
  configureWebpack: {
    resolve: {
      alias: {
        'vue$': '@vue/compat',
        '@': path.resolve(__dirname, 'src'),
      }
    }
  }
});
