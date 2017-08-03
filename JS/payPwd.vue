<style>
  input#hiddenInput {
    transform: scale(.01);
    background: transparent;
    color: transparent;
    border: none;
  }
  .alertPayInfo{z-index:27;position:absolute;left:0;right:0;bottom:0;top:0}
  .alertPayInfo .box{position:absolute;top:20%;left:7%;background:#fff;width:86%;z-index:29;overflow:hidden}
  .alertPayInfo .select{top:auto;bottom:20%}
  .alertPayInfo .alertTitle{height:1.2rem;line-height:1.2rem;background:#003347}
  .alertPayInfo .alertTitle h2{color:#fff;padding:0 .8rem 0 .26667rem;font-size:.42667rem}
  .alertPayInfo .alertTitle .exit{width:.53333rem;height:.53333rem;border:.01333rem solid #fff;border-radius:50%;
    position:absolute;right:.26667rem;top:.32rem;background-size:50% 50%!important}
  .alertPayInfo .alertContent{padding:.4rem .26667rem}.alertPayInfo .alertContent h2{margin-top:.4rem}
  .alertPayInfo .password6{background:#fff;margin-top:.2rem;height:1.13333rem;
    -webkit-box-pack:justify;-ms-flex-pack:justify;-webkit-justify-content:space-between;
    -moz-justify-content:space-between;justify-content:space-between}
  .alertPayInfo .password6 span{display:block;width:15.5%;height:1.13333rem;background-color:#e5e5e5}
  .alertPayInfo .password6 span.select{-webkit-box-align:center;-ms-flex-align:center;
    -webkit-align-items:center;-moz-align-items:center;align-items:center;
    -webkit-box-pack:center;-ms-flex-pack:center;-webkit-justify-content:center;
    -moz-justify-content:center;justify-content:center}
  .alertPayInfo .password6 span.select:after{display:block;content:"\20";width:8px;height:8px;border-radius:50%;background:#333}
  .SlideTopBottom-enter{-webkit-animation:SlideBottomToTop .4s;animation:SlideBottomToTop .4s}
  .SlideTopBottom-leave{-webkit-animation:SlideTopToBottom .4s;animation:SlideTopToBottom .4s}
  @-webkit-keyframes SlideBottomToTop{0%{-webkit-transform:translate3D(0,100%,0);opacity:.5}100%{-webkit-transform:translate3D(0,0,0);opacity:1}}
  @keyframes SlideBottomToTop{0%{transform:translate3D(0,100%,0);opacity:.5}100%{transform:translate3D(0,0,0);opacity:1}}
  @-webkit-keyframes SlideTopToBottom{0%{-webkit-transform:translate3D(0,0,0);opacity:1}100%{-webkit-transform:translate3D(0,100%,0);opacity:.5}}
  @keyframes SlideTopToBottom{0%{transform:translate3D(0,0,0);opacity:1}100%{transform:translate3D(0,100%,0);opacity:.5}}
</style>
<template>
  <!--付款详情-->
  <div class="alertPayInfo" transition="SlideTopBottom" v-if="show">
    <div class="opacity"></div>
    <div class="box">
      <div class="alertTitle">
        <h2>输入支付密码</h2>
        <a href="javascript:;" class="exit" @click="cancelWalletPay"></a>
      </div>
      <div class="alertContent">
        <h2>请输入您的支付密码</h2>
        <div @click="showInput" class="password6">
          <span :class="{'select':payPwd.length>=1}"></span>
          <span :class="{'select':payPwd.length>=2}"></span>
          <span :class="{'select':payPwd.length>=3}"></span>
          <span :class="{'select':payPwd.length>=4}"></span>
          <span :class="{'select':payPwd.length>=5}"></span>
          <span :class="{'select':payPwd.length>=6}"></span>
        </div>
        <input id="hiddenInput" name="password" type="tel" v-model="payPwd" @input="submitPayPwd" maxLength="6"/>
      </div>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        payPwd : "",// 支付密码
        show: true
      }
    },
    name: "pay-info",
    props:['callback'],
    ready:function(){
      document.getElementById("hiddenInput").focus();
    },
    methods: {
      submitPayPwd : function() {
        if (this.payPwd.length >= 6) {
          callback(this.payPwd);
        }
      },
      cancelWalletPay : function() {
        this.show = false
      }
    }
  }
</script>
