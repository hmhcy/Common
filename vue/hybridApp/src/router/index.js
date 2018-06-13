import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home';
import List from '@/components/List';
import Login from '@/components/Login';
import UserCenter from '@/components/user/UserCenter';
import Setting from '@/components/user/Setting';
import PageStack from '@/router/pageStack'
import scrollBehavior from '@/router/scrollBehaviour'

Vue.use(Router)

const extend = (name,cnName,comp) => {
  return { name,cnName,extends: comp }
};

const router = new Router({
  //mode: 'history',
  base: __dirname,
  //scrollBehavior,
  routes: [
    {
      path: '/',
      alis:"index",
      name: 'index',
      component: extend('index',"华艺",Home),
/*      children: [
        {
          path: '',
          name: 'index',
          component: extend('index',"华艺",Home),
        }
      ]*/
    },
    {
      path: '/products',
      name: 'products',
      component: extend('products',"产品页",List),
    },
    {
      path: '/userCenter',
      name: 'userCenter',
      component: extend('userCenter',"用户中心",UserCenter),
      meta:{
        requireLogin:true
      }
    },
    {
      path: '/setting',
      name: 'setting',
      component: extend('setting',"设置",Setting),
      meta:{
        requireLogin:true
      }
    },
    {
      path: '/login',
      name: 'login',
      component: extend('login',"登录",Login)
    }
  ]
});
PageStack.use(router);

/* For Vuex
 *
 * import store from 'store.js';
 *
 * router.beforeEach((to, from, next) => {
 *   // Reset pageStack to the new route
 *   store.commit('navigator/resetStack', to.matched.map(m => m.components.default));
 *   next();
 * });
 */

export default router;
