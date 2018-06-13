<template>
  <page :title ="$options.cnName":right-slot="'disable'">
    <mt-cell title="用户名" :value="userData.userName"></mt-cell>
    <mt-cell title="手机号" :value="userData.phone"></mt-cell>
    <mt-cell title="昵称" :value="userData.nickname || '暂未设置昵称'"></mt-cell>
    <mt-cell title="设置" :to="{name:'setting'}" is-link></mt-cell>
  </page>
</template>

<script>
  export default{
    data(){
      return{
        userData:{}
      }
    },
    mounted(){
      this.$http.get(
        ctx  + "/user/getUser"
      ).then(function(response){
        console.log(response.data)
        if(response.data.code == 1){
          this.userData = response.data.data;
        }
      })
    },
    methods:{
      toSetting(){
        console.log("clicked")
        this.$router.push({name:"setting"})
      }
    }
  }
</script>
