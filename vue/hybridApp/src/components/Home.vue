<template>
  <page :title = "$options.cnName">
    <div class="swipper">
      <mt-swipe :auto="4000">
        <mt-swipe-item>1</mt-swipe-item>
        <mt-swipe-item>2</mt-swipe-item>
        <mt-swipe-item>3</mt-swipe-item>
      </mt-swipe>
    </div>

    <swiper class="swipper" :options="swiperOption">
      <swiper-slide class="rounded-box" v-for="i in 5">Slide {{i}}</swiper-slide>
      <div class="swiper-pagination pagination-fraction" slot="pagination"></div>
    </swiper>

    <div @click="openPicker">
      <ul class="horizontalScroller">
        <li v-for="i in 10">{{i}}</li>
      </ul>
    </div>
    <datePicker :start-date="today" ref="picker" @onselected="pickDate"></datePicker>

   <!-- <mt-tabbar fixed v-model="selected" slot="footer">
      <router-link class="mint-tab-item" id="home" :to="{ name: 'index'}">
        <img slot="icon" src="../assets/logo.png">
        首页
      </router-link>
      <router-link class="mint-tab-item" id="my" :to="{ name: 'userCenter'}" >
        <img slot="icon" src="../assets/logo.png">
        我的
      </router-link>
      <router-link class="mint-tab-item" id="setting" :to="{ name: 'setting'}" >
        <img slot="icon" src="../assets/logo.png">
        设置
      </router-link>
    </mt-tabbar>-->
    <van-tabbar v-model="active">
      <van-tabbar-item icon="home"> <router-link id="home" :to="{ name: 'index'}">
        <img slot="icon" src="../assets/logo.png">
        首页
      </router-link></van-tabbar-item>
      <van-tabbar-item icon="contact" dot><router-link  id="products" :to="{ name: 'products'}" >
        <img slot="icon" src="../assets/logo.png">
        products
      </router-link></van-tabbar-item>
      <van-tabbar-item icon="contact" dot><router-link  id="my" :to="{ name: 'userCenter'}" >
        <img slot="icon" src="../assets/logo.png">
        我的
      </router-link></van-tabbar-item>
    </van-tabbar>
  </page>
</template>

<script>
    import datePicker from '../modules/datePicker'
    export default{
        data(){
            return{
              selected:'home',
              data:"",
              today:new Date(),
              pickerVisible:false,
              swiperOption: {
                slidesPerView: "auto",
//                effect : 'coverflow',
                centeredSlides: true,
                spaceBetween: 15,
                loop: true,
                pagination: {
                  el: '.swiper-pagination',
                  type: 'fraction',
                  renderFraction: function (currentClass, totalClass) {
                    return '<span class="' + currentClass + '"></span>' +
                      ' / ' +
                      '<span class="' + totalClass + '"></span>';
                  },
                },
              }
            }
        },
        components:{
          datePicker
        },
        computed: {
          swiper() {
            return this.$refs.mySwiper.swiper
          }
        },
        mounted(){
          this.$http.post(
            ctx  + "/user/updateUserInfo",
            {
              realName: "HEro"
            }
          ).then(
            function (response) {
              console.log(response.data);
              this.data = response.data.msg
            }
          );
        },
        methods: {
          openPicker() {
            this.$refs.picker.open();
            console.log(this.$refs.picker);
          },
          pickDate(val){
            console.log(val)
          }
        }
    }
</script>
