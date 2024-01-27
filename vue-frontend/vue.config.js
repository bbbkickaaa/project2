const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
transpileDependencies: true,
outputDir: "../src/main/resources/static",
indexPath: "index.html",
devServer: {
port: 8080,
proxy: "http://localhost:8080"
},
chainWebpack: config => {
const svgRule = config.module.rule("svg");
svgRule.uses.clear();
svgRule.use("vue-svg-loader").loader("vue-svg-loader");
},
configureWebpack: {
    resolve: {
      alias: {
        'vue$': '@vue/compat'
      }
    }
  }
})