/**
 * Created by Administrator on 2017/7/3.
 */
import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    counter:0,
    currentLot: {},
    showBuy:false
  },
  // 展示内容, 无法改变状态
  getters: {
    tripleCounter: state => {
      return state.counter * 3;
    }
  },
  // 改变状态
  //mutations 永远是同步的
  mutations: {
    // 显示传递的载荷 payload, 用 num 表示
    increment: (state, num) => {
      state.counter += num;
    },
    toggleView:(state,toggle) => {
      state.showBuy = toggle;
    },
    changeLot:(state,lot) => {
      state.currentLot = lot;
    }
  },
  // 提交 mutation, 这是异步的
  actions: {
    // 显示传递的载荷 payload, 用 asynchNum ( 一个对象 )表示
    asyncDecrement: ({ commit }, asyncNum) => {
      setTimeout(() => {
          // asyncNum 对象可以是静态值
          commit('decrement', asyncNum.by);
      }, asyncNum.duration);
    }
  }
});
