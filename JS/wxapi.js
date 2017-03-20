/**
 * 微信jsapi
 */
export function wxApi() {
  var link = null,//请求数据url （必填）
      title = null,//标题
      desc = null,//描述
      imgUrl = null;//图片路径
  //return {
  function init() {//请求加载数据前准备
    //组装页面加载对象
    //$.extend(def, o);
    ////def.setTitle();
    //def.setLink();
    //def.setDesc();
    //def.setImgUrl();
    $.ajax({//jquery请求数据方法
      url: ctx + "/jzf/weixin/wxConfig.do",
      data: {
        url : location.href.split('#')[0]
      },
      type: 'post',
      dataType: 'json',
      success: function (json) {
        wx.config({
          debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
          appId: json.appId, // 必填，公众号的唯一标识
          timestamp: json.timestamp, // 必填，生成签名的时间戳
          nonceStr: json.nonceStr, // 必填，生成签名的随机串
          signature: json.signature,// 必填，签名，见附录1
          jsApiList:  [
            'checkJsApi',
            'chooseImage',
            'uploadImage',
            'downloadImage',
            'onMenuShareAppMessage',
            'onMenuShareTimeline',
            'onMenuShareQQ',
            'onMenuShareWeibo',
            'onMenuShareQZone'
          ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
        //<%--处理失败验证--%>
        wx.ready(function() {
          // 2. 分享接口
          // 2.1 监听“分享给朋友”，按钮点击、自定义分享内容及分享结果接口
          wx.onMenuShareAppMessage({
            title: "",
            desc: "",
            link: "",
            imgUrl: "",
            trigger: function (res) {
              shareDataInit(this);
            },
            success: function (res) {
            },
            cancel: function (res) {
            },
            fail: function (res) {
            }
          });

          // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
          wx.onMenuShareTimeline({
            title: "",
            link: "",
            imgUrl: "",
            trigger: function (res) {
              shareDataInit(this);
            },
            success: function (res) {
            },
            cancel: function (res) {
            },
            fail: function (res) {
            }
          });

          // 2.3 监听“分享到QQ”按钮点击、自定义分享内容及分享结果接口
          wx.onMenuShareQQ({
            title: "",
            desc: "",
            link: "",
            imgUrl: "",
            trigger: function (res) {
              shareDataInit(this);
            },
            complete: function (res) {
            },
            success: function (res) {
            },
            cancel: function (res) {
            },
            fail: function (res) {
            }
          });

          // 2.4 监听“分享到微博”按钮点击、自定义分享内容及分享结果接口
          wx.onMenuShareWeibo({
            title: "",
            desc: "",
            link: "",
            imgUrl: "",
            trigger: function (res) {
              shareDataInit(this);
            },
            complete: function (res) {
            },
            success: function (res) {
            },
            cancel: function (res) {
            },
            fail: function (res) {
            }
          });

          // 2.5 监听“分享到QZone”按钮点击、自定义分享内容及分享接口
          wx.onMenuShareQZone({
            title: "",
            desc: "",
            link: "",
            imgUrl: "",
            trigger: function (res) {
              shareDataInit(this);
            },
            complete: function (res) {
            },
            success: function (res) {
            },
            cancel: function (res) {
            },
            fail: function (res) {
            }
          });
        });
      }
    });
  }
  function shareDataInit(share) {
    setTitle();
    setLink();
    setDesc();
    setImgUrl();
    share.title = this.title;
    share.link = this.link;
    share.desc = this.desc;
    share.imgUrl = this.imgUrl;
  }
  function setTitle()  {
    if(!this.title) {
      if($("header").find("h2").length > 0) {
        this.title = $("header").find("h2").text();
      }else {
        this.title = "集珍坊";
      }
    }
  }
  function setLink() {
    if(!this.link) {
      this.link = window.location.href;
    }
  }
  function setDesc () {
    if(!this.desc) {
      this.desc = "集珍坊欢迎您";
    }
  }
  function setImgUrl () {
    if(!this.imgUrl) {
      $("img").each(function(){
        var img = $(this);
        var theImage = new Image();
        theImage.src = img.attr("src");

        var imageWidth = theImage.width;
        var imageHeight = theImage.height;
        if(imageWidth >= 400 && imageHeight >= 400) {
          this.imgUrl = img.attr("src");
          return false;
        }
      });
      if(!this.imgUrl) {
        this.imgUrl = ctx + "/static/images/logoBig.jpg";
      }
    }
  }
  init();

  return {
    validation:function(){
      wx.error(function(res){
        return false;
        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。

      });
      wx.checkJsApi({
        jsApiList: ['chooseImage'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
        success: function(res) {
          // 以键值对的形式返回，可用的api值true，不可用为false
          // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
          return res.checkResult.chooseImage;
        }
      });
      return false;
    },
    selfdefinedShare:function(o){
      this.title = o.title;
      this.desc = o.desc;
    },
    chooseImage:function(cb){
      wx.chooseImage({
        /*count: 1, // 默认9
         sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
         sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有*/
        success: function (res) {
          var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
          cb(localIds);
        },
        complete: function(res){
          alert(JSON.stringify(res));
        }
      });
    }
  }
  //};
}
