<template>
  <span>{{timePhrase}}</span>
</template>
<script>
    export default{
        data(){
            return{
              countdown:null,
              day:0,
              hour:0,
              minute:0,
              second:0,
              timePhrase:"",
              startTime:"",
              currentTime:"",
              endTime:"",
              state:0 //0:未开始;1:进行中;2:已结束
            }
        },
        props:{
          options:{
            type: Object,
            default:function(){
              return {
                startTime:"",
                currentTime:"",
                endTime:""
              }
            }
          },
          fmt:{
            type: String,
            default:"day"
          }, //"day , hour, minute, second"
          type:{
            type: Number,
            default:0
          } //0展示倒计时,1展示时间
        },
        destroyed(){
          clearTimeout(this.countdown);//清除计时器
        },
        ready(){
          this.currentTime = (this.options.currentTime!== undefined && this.options.currentTime)? this.options.currentTime.replace(/-/g,"/") : "";
          this.endTime = (this.options.endTime!== undefined && this.options.endTime)? this.options.endTime.replace(/-/g,"/") : "";
          this.startTime =(this.options.startTime!== undefined && this.options.startTime)? this.options.startTime.replace(/-/g,"/") : "";
          this.timer()
        },
        watch:{
          'state'(value){
            var text = "";
            if(value === 0) text = "previewing";
            else if(value === 1) text = "ongoing";
            else text = "ended";
            var el = this.$el;
            this.$dispatch("statechange",text,el)
          },
          'options.endTime'(){
            clearTimeout(this.countdown);//清除计时器
            this.currentTime = (this.options.currentTime!== undefined && this.options.currentTime)? this.options.currentTime.replace(/-/g,"/") : "";
            this.endTime = (this.options.endTime!== undefined && this.options.endTime)? this.options.endTime.replace(/-/g,"/") : "";
            this.startTime =(this.options.startTime!== undefined && this.options.startTime)? this.options.startTime.replace(/-/g,"/") : "";
            this.timer()
          }
        },
        methods:{
          timer:function(){
            var _this = this;
            ///*倒计时*/
            (function(){
              var t = _this.endTime ? new Date(_this.endTime).getTime():0,//取得指定时间的总毫秒数
                n = _this.currentTime ? new Date(_this.currentTime).getTime():0,//取得当前毫秒数
                m = _this.startTime ? new Date(_this.startTime).getTime():0,
                a = t - n, //结束时间点
                b = m - n, //未开始
                c = t - n;//时间差

              if(a<=0){//如果差小于等于0  也就是过期或者正好过期，则退出程序
                clearTimeout(_this.countdown);//清除计时器
                _this.state = 2;
                _this.timePhrase = _this.options.endTime;
                return;
              }
              if(b > 0){
                c=b;
                _this.state = 0;
              }else {
                _this.state = 1;
                c=a;
              }
              var ds = 60*60*24*1000;//一天共多少毫秒
              var d = _this.fmt == "day"? parseInt(c/ds) : 0,//总毫秒除以一天的毫秒 得到相差的天数
                h = _this.fmt == "day" || _this.fmt == "hour"? parseInt((c-d*ds)/(3600*1000)) : 0,//然后取完天数之后的余下的毫秒数再除以每小时的毫秒数得到小时
                m = _this.fmt == "day" || _this.fmt == "hour" || _this.fmt == "minute"? parseInt((c - d*ds - h*3600*1000)/(60*1000)) : 0,//减去天数和小时数的毫秒数剩下的毫秒，再除以每分钟的毫秒数，得到分钟数
                s =  parseInt((c-d*ds-h*3600*1000-m*60*1000)/1000) ;//得到最后剩下的毫秒数除以1000 就是秒数，再剩下的毫秒自动忽略即可

              var dayTemp = d==0 ? "0" : d<10 ? "0" + d : d;
              var hourTemp = h<10 ? "0" + h : h;
              var minuteTemp = m<10 ? "0" + m : m;
              var secondTemp = s<10 ? "0" + s : s;
              if(_this.type == 0){
                _this.timePhrase =  parseInt(dayTemp) > 0 ? dayTemp + "天" + hourTemp+ "小时" + minuteTemp + "分" + secondTemp + "秒"
                                    :parseInt(hourTemp)>0 ? hourTemp+ "小时" + minuteTemp + "分" + secondTemp + "秒"
                                      :parseInt(minuteTemp)>0 ? minuteTemp + "分" + secondTemp + "秒"
                                        :secondTemp + "秒";
              }else if(_this.type == 1){
                _this.timePhrase =  _this.options.startTime;
              }

              var date = new Date(_this.currentTime);//转化为时间戳毫秒数
              date.setTime(date.getTime()+1000);//设置新时间比旧时间多一秒钟
              var year = date.getFullYear();//当前年份
              var month = date.getMonth();//当前月份
              var dates = date.getDate();//天
              var hours = date.getHours();//小时
              var minutes = date.getMinutes();//分
              var seconds = date.getSeconds();//秒
              var newDate = year+"/"+((month+1)<10 ? "0" + (month+1) : (month+1))+"/"+(dates<10 ? "0" + dates : dates)+" "
                +(hours<10 ? "0" + hours : hours)+":"+(minutes <10 ? "0" + minutes : minutes)+":"+(seconds<10 ? "0" + seconds : seconds);
              _this.currentTime =  newDate;

              _this.countdown = setTimeout(_this.timer,1000);
            }());//定义计时器，每隔1000毫秒 也就是1秒 计算并更新 div的显示
          }
        }
    }
</script>
