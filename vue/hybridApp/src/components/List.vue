<style>
  .is-selected{
   border-bottom: 3px solid #d89e30!important;
   color: #d89e30!important;
 }
</style>
<template>
  <page :title = "$options.cnName">
    <swiper class="swipper" :options="swiperOption">
      <swiper-slide class="rounded-box" v-for="i in 5">Slide {{i}}</swiper-slide>
      <div class="swiper-pagination pagination-fraction" slot="pagination"></div>
    </swiper>
    <div>
      <ul class="horizontalScroller">
        <li v-for="index in 5">
          <van-card
                    title="标题"
                    desc="描述"
                    :num="2 * index"
                    price="2.00"
                    :thumb="'/assets/logo.png'"
          />
        </li>
      </ul>
    </div>
    <div id="listbody">
      <van-tabs v-model="selected" swipeable ref="tabs">
        <van-tab v-for="index in 8" :title="'选项 ' + index">
          <van-list
            v-model="loading"
            :finished="finished"
            :offset="offset"
            @load="onLoad"
          >
            <van-card v-for="item in list"
                      title="标题"
                      desc="描述"
                      :num="2 * index"
                      price="2.00"
                      :thumb="'/assets/logo.png'"
            />
          </van-list>
        </van-tab>
      </van-tabs>
    </div>

  </page>
</template>
<script>
    export default{
        data(){
            return{
              selected:"1",
              list: [],
              loading: false,
              finished: false,
              offset:0,
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
        mounted(){
          this.$nextTick(function(){
            console.log(this.$refs.tabs.curActive)
            var hdr = document.querySelector("#listbody").offsetTop - 45
            $(".page").scroll(function() {
              if( $(this).scrollTop() > hdr ) {
                console.log('fixed')
                $(".van-tabs__wrap").addClass("is-fixed")
              } else {
                console.log('not fixed')
                $(".van-tabs__wrap").removeClass("is-fixed")
              }
            });

          })
        },
         methods:{
           onLoad() {
             this.offset = document.querySelector(".van-list").scrollHeight/2;
             console.log(this.offset)
             setTimeout(() => {
             for (let i = 0; i < 10; i++) {
               this.list.push(this.list.length + 1);
             }
             this.loading = false;

             if (this.list.length >= 40) {
               this.finished = true;
             }
           }, 500);
           }
         }
    }
</script>
