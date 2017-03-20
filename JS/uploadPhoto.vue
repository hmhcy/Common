<template>
  <div class="titleInfo">
    <b>上传图片</b>
  </div>
  <div class="photo">
    <div class="add" @click="updloadWx" v-if="wxOrh5"></div>
    <div class="add" v-else><input name="attachment[]" type="file" capture="camera" accept="image/*" @change="updloadImg($event)" multiple="multiple"/></div>
    <div class="list" v-for="(index,img) in imgList">
      <img :src="img" alt="">
      <i @click="delImg(index,$index)"></i>
    </div>
  </div>
</template>
<script>
  import {wxApi} from './wxapi';
  import Vue from 'vue';
  import EXIF from "exif-js"; /*需要安装exif-js*/
  export default{
    data(){
      return{
        imgList:{},
        urlList:[],
        pathList:[],
        mywxApi:new wxApi(),
        wxOrh5:true,
      }
    },
    props: {
      done: {//回调
        type: Function,
        default: function(){}
      }
    },
    ready(){
      this.wxOrh5 = this.mywxApi.validation();
    },
    methods:{
      updloadWx:function(){
        var _self=this;
        this.mywxApi.chooseImage(function(data){
          var i = 0,len=data.length;
          (function uploadImage(){
            Vue.set(_self.imgList,new Date().getSeconds(),data[i]);
            wx.uploadImage({
              localId: data[i],
              isShowProgressTips: 1,
              success: function (res) {
                _self.urlList.push(res.serverId);
                i++;
                if(i === len){
                  _self.done(_self.urlList);
                }else if(i < len){
                  uploadImage();
                }
              },
              fail: function (res) {
                alert(JSON.stringify(res));
              }
            });
          })();
        });
      },
      updloadImg:function(event){
        var _this = this,files=event.target.files;
        if (files.length === 0) { return false; }
        if(length > 5){
          mAlaert.run({
            id : "mAlaert",
            content : '最多上传六张图片,请重新选择',
            time : 2000
          });
          return;
        }
        for(var i= 0;i<files.length;i++){
          let file = files[i];
          let name = file.name;
          if(name=="image.jpg"){
            name="image"+_this.uuid()+".jpg";
          }
          let orientation="";
          //EXIF js
          EXIF.getData(file,function() {
            orientation=EXIF.getTag(this,'Orientation');
          });
          var reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload=function(e){
            _this.getFiles(name,orientation, e.target.result);
          }
        }
        _this.done(_this.urlList);
      },
      getFiles(name,orientation,base64){
        var _that =this;
        if(Object.keys(_that.imgList).length > 5) delete _that.imgList[Object.keys(_that.imgList)[0]]
        if(_that.imgList[name]!==undefined) return false;
        Vue.set(_that.imgList,name,base64);
        var img = new Image();
        img.src = base64;
        img.onload = function(){
          _that.getImgData(this,orientation,function(data){
            _that.urlList.push(data);
          })
        };
      },
      getImgData:function(img,orientation,callback) {
        var MAX_WIDTH = 1024;
        var MAX_HEIGHT = 1024;
        var degree=0;
        var tempW,tempH;
        var w = img.width;
        var h = img.height;
        if (w > h) {
          if (w > MAX_WIDTH) {
            h *= MAX_WIDTH / w;
            w = MAX_WIDTH;
          }
        } else {
          if (h > MAX_HEIGHT) {
            w *= MAX_HEIGHT / h;
            h = MAX_HEIGHT;
          }
        }
        var cvs = document.createElement('canvas');
        cvs.width = tempW = w;
        cvs.height = tempH = h;
        var ctx = cvs.getContext('2d');
        switch(orientation){
          //iphone横屏拍摄，此时home键在左侧
          case 3:
            degree=180;
            tempW=-w;
            tempH=-h;
            break;
          //iphone竖屏拍摄，此时home键在下方(正常拿手机的方向)
          case 6:
            cvs.width=h;
            cvs.height=w;
            degree=90;
            tempW=w;
            tempH=-h;
            break;
          //iphone竖屏拍摄，此时home键在上方
          case 8:
            cvs.width=h;
            cvs.height=w;
            degree=270;
            tempW=-w;
            tempH=h;
            break;
        }
        ctx.rotate(degree * (Math.PI/180));
        ctx.drawImage(img,0, 0, tempW, tempH);
        try{
          var dataurl = cvs.toDataURL("image/jpeg");
        }catch(e){
          alert(e);
        }
        callback(dataurl);
      },
      delImg:function(key,idx){
        Vue.set(this.imgList,key,"");
        delete this.imgList[key]
        this.urlList.splice(idx,1);
      },
      uuid:function () {
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
          s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "-";

        var uuid = s.join("");
        return uuid;
      }
    }
  }
</script>
