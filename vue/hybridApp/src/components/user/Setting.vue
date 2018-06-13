<template>
  <page :title = "$options.cnName":right-slot="'disable'">
    <mt-cell title="修改资料" to="setting" is-link value="修改资料"></mt-cell>
    <mt-cell title="修改密码" to="setting" is-link value="修改密码"></mt-cell>
    <mt-cell title="我要吐槽" to="setting" is-link value="我要吐槽"></mt-cell>
    <mt-cell title="关于我们" to="setting" is-link value="关于我们"></mt-cell>
    <mt-button type="default" size="large">退出登录</mt-button>
  </page>
</template>
<script>
  export default{
    data(){
      return{
        data:""
      }
    },
    mounted(){

    }
  }
</script>

