// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import router from './router'
import App from './App'
import Resource from 'vue-resource'
import config from './js/config'
import {beforeEach, afterEach} from './js/util'
import interceptor from '@/router/interceptor'
import { store } from './store/store'
import lazyImg from './js/lazyLoad';
//import '../static/font-awesome/font-awesome.min.css'
//import header from '@/modules/commonHeader';
import page from '@/modules/page';

/** UI Components **/
import VueAwesomeSwiper from 'vue-awesome-swiper';
import 'swiper/dist/css/swiper.css';
import MintUI from 'mint-ui'
import 'mint-ui/lib/style.css'
import Vant from 'vant';
import 'vant/lib/vant-css/index.css';

Vue.use(Vant);
Vue.use(MintUI)
Vue.use(VueAwesomeSwiper)

/** UI Components **/

Vue.use(Resource)
Vue.config.productionTip = false
Vue.http.options.emulateJSON = true
Vue.http.options.xhr = {withCredentials: true};
Vue.http.options.credentials = true;
//router.beforeEach((to, from, next) => { beforeEach(to, from, next) })
//router.afterEach((to, from)  => { afterEach(to, from) })
Vue.http.interceptors.push((request, next) => { interceptor(request, next, router) })

Vue.component('v-img', lazyImg);
//Vue.component('common-header',header);
Vue.component('page',page);
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})
