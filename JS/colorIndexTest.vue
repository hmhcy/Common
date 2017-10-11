<template>
  <div class="specialScroll">
    <ul style="width: 999rem;">
      <li v-for="c in jzfexclusive.commodityList">
        <a v-link="'/commoditycommodityDetail?id=' + c.commodityId">
          <img class="colorAnalysis" :src="url+c.commodityImg" alt="" @load="colorAnalyse($event)"/>
          <span class="textResult">{{c.maxValuation | rmb}}</span>
        </a>
      </li>
      <li class="more">
        <a v-link="{path:'/jzfExclusive',query:{id:jzfexclusive.homepageWeixinAddress}}">查看更多</a>
      </li>
    </ul>
  </div>
</template>
<script>
/*  window.onload = function() {
    var target = document.querySelector('.specialScroll ul li a');
    console.log(target);
    var observer = new MutationObserver(function(mutations) {
      console.log(mutations);
    });
    var config = {
      childList: !0
    };
    observer.observe(target, config);
/!*    var t = setInterval(function(){
      var img = $('img[data-key="imgTest0"]');
      var text = $('span[data-key="text0"]');
      var img1 = $('img[data-key="imgTest1"]');
      var text1 = $('span[data-key="text1"]');
      if(img.length>0 && img1.length>0){
        clearInterval(t);
        getImgData(img[0],text);
        getImgData(img1[0],text1);
      }
    }, 500);*!/
  }*/

 function getImgData(img,text){
   var w = img.width;
   var h = img.height;
   var th = text.height();
   var cvs = document.createElement('canvas');
   // resize the canvas for drawing
   cvs.width = w;
   cvs.height = h;
   var ctx = cvs.getContext('2d');
   // render the image to the canvas in order to obtain image data
   ctx.drawImage(img, 0, 0);
   var data = ctx.getImageData(0, 0, w, h).data;
   var brightness = 0;
   var sX = 0, sY = Math.round(h-th), eX = w, eY = h;
   var idx=0;
   var i = 0;
   for(var y=sY;y<h;y++) {
     for(var x=0;x<w;x++) {
       idx = (y * w + x) * 4;
       var r = data[idx],
         g = data[idx + 1],
         b = data[idx + 2];
       brightness += Math.round(((parseInt(r) * 299) + (parseInt(g) * 587) + (parseInt(b) * 114)) /1000);
       i++;
     }
   }
   if (brightness !== 0) brightness /= i;
   if (brightness > 125) var textColor = "black";
   else var textColor = "white";
   text.css("color", textColor);
   // clear up the canvas
   ctx.clearRect(0, 0, w, h);
 }


  /*
  var querystring = require('querystring');
  var str = 'nick=casper&age=24';
  var obj = querystring.parse(str);
  console.log(JSON.stringify(obj, null, 4));*/

  var inter = "";
  export default {
    data () {
      return {
        list:{},
        jzfexclusive:new Array(),//集珍坊自营
        url:"",
       }
    },
    methods:{
      colorAnalyse:function(e){
        e.target.crossOrigin = "Anonymous"
        console.log($(e.target));
        var img = $(e.target);
        var text = $(e.target).siblings("span");
        getImgData(img[0],text);
      }
    }
  }
</script>
