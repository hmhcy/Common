<template>
  <page :title = "$options.cnName":right-slot="'disable'">
    <mt-cell title="头像">修改资料</mt-cell>
    <mt-cell title="昵称" value="昵称"></mt-cell>
    <mt-cell title="手机号" value="昵称"></mt-cell>
  </page>
</template>
<script>
    export default{
        data(){
            return{
                msg:'hello vue'
            }
        },
        methods:{

        }
    }
</script>
