<style src="./assets/css/public.css"></style>
<template>
  <div id="app" :class="deviceClass" @touchstart="touchStart" @touchend="touchEnd">
    <transition name="page" v-if="enbleTransition">
      <keep-alive include="index">
        <router-view></router-view>
      </keep-alive>
      <!--class="child-view"-->
    </transition>
    <router-view v-if="!enbleTransition"></router-view>
  </div>
</template>
<script>
  import { Toast } from 'mint-ui';
  import Util from './js/util';
  export default {
    name: 'app',
    render :function(createElement){
      var a = this.elementtype.split(",");
      console.log(this.$slots.default)
      return createElement(a[0],{
          attrs:{
            id:a[3],
            style:"color:"+a[1]+";font-size:"+a[2]+";"
          }
        },
        this.$slots.default
      )
    },
    data() {
      return {
        pageStack: [],
        deviceClass: '',
        touchTime:0,
        enbleTransition:true
      }
    },
    methods: {
      touch(){
        console.log("touch")
      },
      touchStart(e){
        this.touchTime = Date.now()
      },
      touchEnd(e){
//        alert(Date.now() - this.touchTime)
        /*Toast({
          message: Date.now() - this.touchTime + "&&&&&" + e.changedTouches[0].pageX,
          duration: 300
        });*/
        if(Date.now() - this.touchTime > 100 && e.changedTouches[0].pageX < 0 ){
          this.deviceClass = ""
          this.enbleTransition = false
        }else{
          this.enbleTransition = true
          if (Util.browser.isIos()) {
            this.deviceClass = 'ios-device'
          } else {
            this.deviceClass = 'android-device'
          }
        }
      }
    },
    mounted() {
      console.log(this)
    },
    created() {
      if (Util.browser.isIos()) {
        this.deviceClass = 'ios-device'
      } else {
        this.deviceClass = 'android-device'
      }
      /*const mapRouteStack = route => {
        this.pageStack.extends(route.matched.map(m => m.components.default))
      };
      mapRouteStack(this.$route);
      /!* On route change, reset the pageStack to the next route *!/
      this.$router.beforeEach((to, from, next) => {
        mapRouteStack(to)
        console.log(this.pageStack)
        next()
      });*/
    }
  }
</script>
