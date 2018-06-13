<style>
  .content{
    margin:1.3rem 0 1rem;
    padding: 0 5px 5px;
    z-index:0;
    /*overflow: scroll;*/
    /*-webkit-overflow-scrolling: touch;*/
    /*overflow-x:hidden;*/
  }
  .page >header{
    height:1.2rem;
    background-color: #fff;
    color:#000;
  }
  .placeholder{
    flex: 1;
    min-height:.5rem;
    max-height:1rem;
  }
</style>
<template>
  <div class="page">
    <mt-header fixed :title="title">
      <slot name="left">
        <div @click="$router.back()" slot="left" v-show="leftSlot!=='disable' && $router._pages.length > 1">
          <mt-button>
            <i class="fa fa-chevron-circle-left fa-2x" style="color: rgba(0,0,0,0.20);padding-left: .1rem;width:30px" aria-hidden="true"  slot="icon"></i>
          </mt-button>
        </div>
      </slot>
      <slot name="right" >
        <mt-button slot="right" v-show="rightSlot!=='disable'">分享</mt-button>
      </slot>
    </mt-header>
    <!--占位div-->
    <!--<div class="placeholder"></div>-->
    <article class="content" ref="content">
      <slot></slot>
    </article>
    <!--占位div-->
    <!--<div class="placeholder"></div>-->
    <slot name="footer"></slot>
  </div>
</template>
<script>
  export default {
    name: 'page',
    props:["title","rightSlot","leftSlot"],
    data() {
      return {
        scrollTop: 0,
        selected:"1"
      }
    },
    mounted() {
      console.log(this.$router)
      this.$refs.content.scrollTop = this.scrollTop
    },
    methods: {
      scroll() {
        this.scrollTop = this.$refs.content.scrollTop
      }
    }
  }
</script>
