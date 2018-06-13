<template>
  <mt-header fixed :title="title">
    <slot name="left">
      <div @click="$router.back()" slot="left">
        <mt-button icon="back"></mt-button>
      </div>
    </slot>
    <slot name="right">
      <mt-button slot="right">分享</mt-button>
    </slot>
  </mt-header>
</template>
<script>

    export default{
        data(){
            return{

            }
        },
        props:["title"]
    }
</script>
