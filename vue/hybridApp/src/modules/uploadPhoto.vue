<template>
  <slot></slot>
  <input style="display: none" id="hiddenInput" name="attachment" type="file" accept="image/*" @change="updloadImg($event)" multiple/>
</template>
<script>
  import Vue from 'vue';
  import EXIF from "exif-js";
  import { Toast } from 'mint-ui';
  export default{
    data(){
      return{
        fileLen:0,
        urlList:[],
        pathList:[],
        wxOrh5:false,
      }
    },
    props: {
      done: { //callback
        type: Function,
        default: function(){}
      },
      limit:{ //最多上传照片张数
        type:Number,
        default: 4
      },
      container:{ //自定义上传按钮样式名称
        type:String,
        default: "add"
      },
      delIcon:{
        type:String,
        default: ""
      }
    },
    ready(){
      try {//app设置高度
        app.setMaxImageUpload(this.limit);

      } catch (exception) {
      }
      var _this = this;
      if(api.type==="wx" && this.limit > 1){
        api.imageSelectorEligible(function(validate){
          _this.wxOrh5 = validate;
          if(validate){
            document.querySelector("."+_this.container).addEventListener("click",_this.updloadWx);
          }else{
            document.querySelector("."+_this.container).addEventListener("click",_this.buggyUpload);
          }
        });
      }else{
        _this.wxOrh5 = false;
        document.querySelector("."+this.container).addEventListener("click",this.buggyUpload);
      }

     /* this.$nextTick(function(){
        alert(this.wxOrh5)
        if(this.wxOrh5){
          document.querySelector("."+this.container).addEventListener("click",this.updloadWx);
        }else{
          document.querySelector("."+this.container).addEventListener("click",this.buggyUpload);
        }
      })*/
    },
    methods:{
      buggyUpload:function(){
//        Android.showToast("you have clicked upload!");
        $("#hiddenInput").click();

      },
      updloadWx:function(){
        var _self=this;
        api.chooseImage(_self.limit,function(data){
            var i = 0,len=data.length;
            _self.urlList = [];
            (function uploadImage(){
//              Vue.set(_self.imgList,new Date().getSeconds(),data[i]);
              wx.getLocalImgData({
                localId: data[i],
                success: function (res) {
//                  let temp = {docId : res.serverId,docAdd:data[i]}
                  let localData = res.localData;

                  if(!window.__wxjs_is_wkwebview && !/iPad|iPhone|iPod/.test(navigator.userAgent)){
                    if(!/^data:image/.test(localData)){
                      localData = "data:image/jpg;base64," + localData
                    }
                  }
                  let temp = {docId : -1,docAdd:localData}
                  _self.urlList.push(temp);
                  i++;
                  if(i === len){
                    _self.done(_self.urlList,_self.wxOrh5);
                  }else if(i < len){
                    uploadImage();
                  }
                  /*_self.getFiles(0, localData, function(temp){
                    _self.urlList.push(temp);
                    i++;
                    if(i === len){
                      _self.done(_self.urlList,_self.wxOrh5);
                    }else if(i < len){
                      uploadImage();
                    }
                  });*/
                },
                fail: function (res) {
//                    alert(JSON.stringify(res));
                }
              });
            })();
          },
          function(){
            _self.wxOrh5 = false;
            document.querySelector("input[name='attachment']").click();
          });
      },
      updloadImg:function(event){
        var _this = this,files=event.target.files;
        this.urlList = [];
        this.fileLen = files.length;
        if (files.length === 0) { return false; }
        if(files.length > _this.limit){
          Toast({
            message: '最多上传'+_this.limit+'张图片,请重新选择',
            duration: 2000
          });
          return;
        }

        let i = 0,l=files.length;
        (function processImage(){
          let file = files[i];
          let orientation="";
          //EXIF js
          EXIF.getData(file,function() {
            orientation=EXIF.getTag(this,'Orientation');
          });
          var reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload=function(e){
            _this.getFiles(orientation, e.target.result, function(result){
              _this.urlList.push(result);
              i++;
              if(i === l){
                _this.done(_this.urlList,_this.wxOrh5);
              }else if(i < l){
                processImage();
              }
            });
          }
        })();
/*        for(var i= 0;i<files.length;i++){
          let file = files[i];
          let index = i;
          let orientation="";
          //EXIF js
          EXIF.getData(file,function() {
            orientation=EXIF.getTag(this,'Orientation');
          });
          var reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload=function(e){
            _this.getFiles(index,orientation, e.target.result);
          }
        }*/
      },
      getFiles(orientation,base64,callback){
        var _that =this;
       /* if(Object.keys(_that.imgList).length > _that.limit-1) delete _that.imgList[Object.keys(_that.imgList)[0]]
        if(_that.imgList[name]!==undefined) return false;
        Vue.set(_that.imgList,name,base64);*/
        var img = new Image();
        img.src = base64;
        img.onload = function(){
          _that.getImgData(this,orientation,function(data){
            let temp = {docId : -1,docAdd:data}
            callback(temp)
          })
        };
      },
      getImgData:function(img,orientation,callback) {
        var MAX_WIDTH = 1000;
        var MAX_HEIGHT = 1000;
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
        //使用canvas旋转校正
//           alert(orientation)
        ctx.rotate(degree * (Math.PI/180));
//           alert(Math.PI/180);
        ctx.drawImage(img,0, 0, tempW, tempH);
        try{
          var dataurl = cvs.toDataURL("image/jpeg");
        }catch(e){
              console.log(e);
        }
        callback(dataurl);
      },
      getCompressRate:function(allowMaxSize,fileSize){ //计算压缩比率，size单位为MB
        var compressRate = 1;
        if(fileSize/allowMaxSize > 4){
          compressRate = 0.5;
        } else if(fileSize/allowMaxSize >3){
          compressRate = 0.6;
        } else if(fileSize/allowMaxSize >2){
          compressRate = 0.7;
        } else if(fileSize > allowMaxSize){
          compressRate = 0.8;
        } else{
          compressRate = 0.9;
        }
        return compressRate;
      },
      delImg:function(idx){
        this.urlList.splice(idx,1);
      }
    },
    events:{
      delImgFromParent:function(idx){
        this.delImg(idx);
      },
      updateList:function(oidx,nidx){
//        var temp =  this.urlList[oi/dx]
        this.urlList.move(oidx,nidx);
      },
      clearList:function(){
        this.urlList = []
      }
    }
  }

</script>
