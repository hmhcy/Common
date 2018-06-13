<style scoped>
  .captcha{
    padding: 0.23333rem;
    background: #003347;
    width: 2.3rem;
    text-align: center;
    font-size: 0.34333rem;
    display: inline-block;
    -webkit-box-sizing: content-box;
    -moz-box-sizing: content-box;
    box-sizing: content-box;
    border: none;
    -webkit-border-radius: 50px / 53px;
    border-radius: 50px / 53px;
    color: rgba(0,0,0,0.9);
    -o-text-overflow: clip;
    text-overflow: clip;
    background: rgba(237,237,237,1);
  }
  .page-part{
    background-color: #eee;
    margin: 10vh 0 0;
  }
  .page-part .mint-tab-item.is-selected{
   border-bottom: 1px solid #eee;
  }
</style>
<template>
  <page :title = "$options.cnName" :right-slot="'disable'">
    <mt-tab-container v-model="active" class="hy-default-content-bg">
      <mt-tab-container-item id="smsLogin">
        <mt-cell title="手机号登录"><i slot="icon" class="fa fa-reg fa-fw" aria-hidden="true"></i></mt-cell>
        <mt-cell class="mint-field" title="手机号"><i slot="icon" class="mbri-mobile mint-cell-text" aria-hidden="true"></i>
          <input class="mint-field-core" placeholder="请输入手机号" type="tel" v-model="phone" debounce="500" ref="phoneInput"/>
        </mt-cell>
        <!--<mt-field label="手机号" placeholder="请输入手机号" type="tel" v-model="phone" debounce="500"> </mt-field>-->
        <mt-cell class="mint-field" title="验证码"> <span slot="icon" class="mint-cell-text" :class="(phoneValidate || countdown > 0) ? 'mbri-lock' : 'mbri-unlock'"></span>
          <input class="mint-field-core" placeholder="请输入验证码" v-model="captcha" debounce="500"/>
          <div :class="['captcha', {'disable':phoneValidate || countdown > 0}] " @click="sendSms">{{sendMessage}}</div>
        </mt-cell>
        <!--<mt-cell>未注册华艺的手机号，登录时将自动注册</mt-cell>-->
        <van-cell style="font-size:12px;line-height: 12px;color: #B4B4B4;" value="未注册华艺的手机号，登录时将自动注册" />
      </mt-tab-container-item>
      <mt-tab-container-item id="pwdLogin">
        <mt-cell title="密码登录" icon="more"></mt-cell>
        <mt-field label="用户名" placeholder="请输入用户名" v-model="userName"></mt-field>
        <mt-field label="密码" placeholder="请输入密码" type="password" v-model="pwd"></mt-field>
      </mt-tab-container-item>
    </mt-tab-container>
    <mt-button class="hy-default-button hy-default-color" type="primary" size="large" @click="login">登录</mt-button>
    <mt-navbar class="page-part" v-model="active">
      <mt-tab-item>
        <img slot="icon" src="../assets/logo.png"><span>微信登录</span>
      </mt-tab-item>
      <mt-tab-item  v-show="active=='pwdLogin'" id="smsLogin">
        <img slot="icon" src="../assets/logo.png"><span>手机登录</span>
      </mt-tab-item>
      <mt-tab-item  v-show="active=='smsLogin'" id="pwdLogin">
        <img slot="icon" src="../assets/logo.png" v-show="active=='smsLogin'"><span>密码登录</span>
      </mt-tab-item>
    </mt-navbar>
  </page>
</template>

<script>
  import { Toast } from 'mint-ui';
  export default{
    data(){
      return{
        active:"smsLogin",
        userName:'',
        pwd:"",
        phone:"",
        captcha:"",
        sendMessage:"发送验证码",
        countdown:0,
        redirect:this.$route.query.redirect || "index"
      }
    },
    mounted(){
      this.$nextTick(function () {
        this.$refs.phoneInput.focus();
      })
    },
    computed:{
      'phoneValidate'(){
        return  !this.phone.validate("tel")
      }
    },
    methods:{
      sendSms(){
        _this.sendMessage = "发送中..."
        this.$http.post(
          ctx  + "/sms/getCaptchaCode",
          {
            userPhone: this.phone
          }
        ).then(function (response) {
            var _this = this;
            this.countdown = 60;
            const countdown = setInterval(function(){
              if(_this.countdown <= 0){
                _this.sendMessage = "发送验证码";
                _this.countdown = 60;
                _this.phoneValidate = true;
                clearInterval(countdown);
                return;
              }else{
                _this.phoneValidate = false;
                _this.countdown--;
                _this.sendMessage = _this.countdown+"s后重新发送";
              }
            },1000);
            Toast({
              message: response.data.msg,
              duration: 1000
            });
        })

      },
      login(){
        if(this.active === "smsLogin"){
          this.$http.post(
            ctx  + "/user/login/sms",
            {
              userPhone: this.phone,
              captcha: this.captcha
            }
          ).then(
            function (response) {
              Toast({
                message: response.data.msg,
                duration: 1000
              });
              if(response.data.code === 1){
                localStorage.setItem("userLogged",true);
                this.$router.push({name:"index"});
              }
            }
          ).then(
            function (response) {
              Toast({
                message: response.data.msg,
                duration: 1000
              });
            }
          );
        }else{
          this.$http.post(
            ctx  + "/user/login",
            {
              userName: this.userName,
              pwd: this.pwd,
              rememberMe:1
            }
          ).then(
            function (response) {
              Toast({
                message: response.data.msg,
                duration: 1000
              });
              if(response.data.code === 1){
                localStorage.setItem("userLogged",true);
                this.$router.push({name:"index"});
              }
            }
          ).then(
            function (response) {
              Toast({
                message: response.data.msg,
                duration: 1000
              });
            }
          );
        }

      }
    }
  }
</script>
