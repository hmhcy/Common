<template>
  <mt-picker v-show="pickerVisible" class="mint-popup mint-datetime mint-popup-bottom"
             :slots="slots"
             :show-toolbar="true"
             ref="myPicker">
    <div class="picker-toolbar">
      <span class="mint-datetime-action mint-datetime-cancel" @click="close">取消</span>
      <span class="mint-datetime-action mint-datetime-confirm" @click="confirm">确定</span>
    </div>
  </mt-picker>
</template>
<script>
   import Util from '../js/util';
    export default{
      data(){
          return{
            slots: [],
            dateList:[],
            pickerVisible:false,
            selected:""
          }
      },
      props:["startDate"],
      activated(){
        console.log(this.startDate)
        this.dateList = Util.createTime(this.startDate.toString(),7,true,"dd");
        this.slots =[{
          flex: 1,
          values: [this.startDate.getFullYear()],
          textAlign: 'center'
        },{
          divider: true,
          content: '年',
        }, {
          flex: 1,
          values: [this.startDate.getMonth()+1],
          textAlign: 'center'
        },{
          divider: true,
          content: '月',
        }, {
          flex: 1,
          values: this.dateList,
          textAlign: 'center'
        }]
      },
      methods:{
        open(){
          this.pickerVisible = true;
        },
        close(){
          this.pickerVisible = false;
        },
        confirm(){
          console.log(this.$refs.myPicker.getValues())
          this.pickerVisible = false;
          this.selected = this.$refs.myPicker.getValues().join("/")
          this.$emit('onselected',this.selected);
        },
        getValues(){
          return this.selected;
        }
      }
    }
</script>
