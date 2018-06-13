Date.prototype.Format = function(fmt)
{
  var o = {
    "M+" : this.getMonth()+1,                 //月份
    "d+" : this.getDate(),                    //日
    "h+" : this.getHours(),                   //小时
    "m+" : this.getMinutes(),                 //分
    "s+" : this.getSeconds(),                 //秒
    "q+" : Math.floor((this.getMonth()+3)/3), //季度
    "S"  : this.getMilliseconds()             //毫秒
  };
  if(/(y+)/.test(fmt))
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
  for(var k in o)
    if(new RegExp("("+ k +")").test(fmt))
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
  return fmt;
}

String.prototype.validate = function(type){
  var phoneReg = /^1(3|4|5|6|7|8|9)\d{9}$/
  var idCardReg = /^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$/
  if(type === "tel"){
    return phoneReg.test(this);
  }
  if(type === "id"){
    return idCardReg.test(this);
  }
}

Object.defineProperty(Array.prototype, 'findExistence', {
  configurable: false,
  enumerable: false,
  writable: false,
  value: function(obj) {
    if(typeof obj.length === "undefined"){
      obj = [obj];
    }
    var i = 0,l = this.length,r = obj.length,duplicate=false;
    var j = 0;
    for(j; j<r; j++){
      duplicate=false
      for(i;i<l;i++){
        if(this[i]===obj[j]){
          duplicate=true
          break;
        }
      }
      if(duplicate){
        break;
      } else {
        continue;
      }
    }
  }
});
Object.defineProperty(Array.prototype, 'extends', {
  configurable: false,
  enumerable: false,
  writable: false,
  value: function(obj) {
    if(typeof obj.length === "undefined"){
      obj = [obj];
    }
    var i = 0,l = this.length,r = obj.length,duplicate=false;
    var j = 0;
    for(j; j<r; j++){
      duplicate=false
      for(i;i<l;i++){
        if(this[i]===obj[j]){
          duplicate=true
          break;
        }
      }
      if(duplicate){
        this.splice(i,l-i,obj[j]);
      } else {
        this.push(obj[j])
      }
    }
  }
});
/**
 * 浏览器类型
 * @type {{isWeixin: browser.isWeixin, isAndroid: browser.isAndroid, isIos: browser.isIos}}
 */


/**
 * 关注弹窗
 */
export var mAlert = {};
mAlert.setting = {
  id: "mAlert",
  content: "内容",
  time: 3000
};
mAlert.run = function (setting) {
  if (!setting.id) {
    setting.id = this.setting.id;
  }
  if (!setting.content) {
    setting.content = this.setting.content;
  }
  if (!setting.time) {
    setting.content = this.setting.time;
  }
  var html = [];

  html += '<div class="tipInfoCont" id="' + setting.id + '"><p>' + setting.content + '</p></div>';
  $("body").append(html);
  //显示时间
  var t = setTimeout(function () {
    $("#" + setting.id).remove();
  }, setting.time);
};


/********************Vue filter start*********
 *
 * ***********************************************************************************/
import Vue from 'vue';

/**
 * 路由进入后拦截器
 * @param route
 */
export function afterEach(to, from) {
  $('.loading').hide();
  Vue.nextTick(function () {
    var counter = setInterval(function () {
      if (document.querySelector("article.main").scrollHeight >= document.body.clientHeight) {
        clearInterval(counter)
        if (to.meta["savedPosition"]) {
          document.querySelector("article.main").scrollTop = to.meta["savedPosition"]
        }
      }
    }, 30)
  })
}

//头像过滤
Vue.filter('avatar', function (value,url,annony,type) {
  var defaultAvatar = "../../../static/images/defaultavatar.png",
    adminAvatar = "../../../static/images/userhead1.jpg"
  if(annony == 1) return defaultAvatar;
  if(!value || value === undefined){
    if(type == "admin") return adminAvatar;
    else return defaultAvatar;
  }
  var pat = /^http/i;
  if(pat.test(value)) return value
  else return url+value
})

Vue.filter('url', function (value,url) {
  var defaultFail = "../../../static/images/loadFailure.png"
  if(!value || value === undefined){
    return defaultFail
  }
  var pat = /^http/i;
  if(pat.test(value)) return value
  else return url+value
})
//昵称过滤
Vue.filter('hideName', function (value, exceptionList) {
  if(value===undefined) return "匿名用户"
  if(exceptionList && exceptionList.indexOf(value) > -1) return value;
  if(/^(13|14|15|17|18)\d{9}$/.test(value)) value =  value.slice(0,3)+"*****"+value.slice(-3);
  else value =  value.slice(0,1)+"**"+value.slice(-1);
  return value;
})
//时差格式
Vue.filter('timeDiff', function (value,now) {
  value= value!==undefined?value.replace(/-/g,"/"):value;
  now = now!==undefined?now.replace(/-/g,"/"):now;
  var type = 0,// 0:xx时间前,1:xx时间后
    t = new Date(value).getTime(),//取得指定时间的总毫秒数
    n = new Date(now).getTime(),//取得当前毫秒数
    c = n - t;//得到时间差
  if(c <= 0) {
    type = 1;
    c = t - n;
  }
  var minute = 1000 * 60,
    hour = 1000 * 60 * 60,
    day = 1000 * 60 * 60 * 24,
    month = 1000 * 60 * 60 * 24 * 30,
    year = 1000 * 60 * 60 * 24 * 30 * 12;

  if(type === 0 && c < minute) return "刚刚";
  var s = Math.floor(c/1000),//得到最后剩下的毫秒数除以1000 就是秒数，再剩下的毫秒自动忽略即可
    m = Math.floor(c/minute),//减去天数和小时数的毫秒数剩下的毫秒，再除以每分钟的毫秒数，得到分钟数
    h = Math.floor(c/hour),//然后取完天数之后的余下的毫秒数再除以每小时的毫秒数得到小时
    d = Math.floor(c/day),//总毫秒除以一天的毫秒 得到相差的天数
    w = Math.floor(c/(day * 7)),
    mm = Math.floor(c/month),
    y = Math.floor(c/year);
  if(type === 0){
    return (y > 0?y+"年前":(mm > 0? mm + "月前":(w > 0?w+"周前":(d > 0?d+"天前":(h > 0 ?h+"小时前":(m > 0 ?m+"分钟前":s+"秒前"))))));
  }else if(type === 1){
    return (y > 0?y+"年后":(mm > 0? mm + "月后":(w > 0?w+"周后":(d > 0?d+"天后":(h > 0 ?h+"小时后":(m > 0 ?m+"分钟后":s > 0?s+"秒后":"1秒后"))))));
  }
  /*
   else if(type === 1) return (y > 0?y+"年后":"" + mm > 0? mm + "月后":"" + w > 0?w+"周后":"" + d > 0?d+"天后":"" + h > 0 ?h+"小时后":"" + m > 0 ?m+"分后":"" + s+"秒后");
   */
})

//字数过滤器
Vue.filter('wordLimit', function (value,param) {
  var subtract = value.substr(0,param);
  if(value.length > param) subtract += "..."
  return subtract;
})
//价钱人民币显示过滤器
Vue.filter('rmb', function (value,msg) {
  if(value===0 || value===undefined){
    return msg || "估价待询";
  }
  value =value+"";
  var s = (s = value.indexOf(".")>0?value.substr(0, value.indexOf(".")).length:value.length) > 3 ? s % 3 : 0;
  return " ¥ "+(s ? value.substr(0, s) + "," : "")
    + value.substr(s).replace(/(\d{3})(?=\d)/g, "$1" + ",");
});

//数字显示过滤器
Vue.filter('number', function (value) {
  if(value===0 || value===undefined){
    return 0;
  }
  value =value+"";
  var s = (s = value.indexOf(".")>0?value.substr(0, value.indexOf(".")).length:value.length) > 3 ? s % 3 : 0;
  return (s ? value.substr(0, s) + "," : "")
    + value.substr(s).replace(/(\d{3})(?=\d)/g, "$1" + ",");
});

//时间
Vue.filter('ymd', function(value){
  var date = new Date(Date.parse(value.replace(/-/g,"/")));
  return date.getFullYear()+"."+(date.getMonth()+1)+"."+date.getDate();
});
//时间
Vue.filter('nyr', function(value){
  if(!value) return false;
  var date = new Date(Date.parse(value.replace(/-/g,"/")));
  return date.getFullYear()+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日   "+
    (date.getHours() < 10 ? "0"+date.getHours() : date.getHours()) + ":" + (date.getMinutes() < 10 ? "0"+date.getMinutes() : date.getMinutes());
});
//时间
Vue.filter('time', function(value){
  var date = new Date(Date.parse(value.replace(/-/g,"/")));
  return (date.getMonth()+1)+"月"+date.getDate()+"日   "+(date.getHours() < 10 ? "0"+date.getHours() : date.getHours()) + ":" + (date.getMinutes() < 10 ? "0"+date.getMinutes() : date.getMinutes());
});
/********************Vue filter end*********
 *
 * ***********************************************************************************/


 var browser = {
  isWeixin: function () {
    if (/MicroMessenger/i.test(navigator.userAgent)) { //这是微信平台下浏览器
      return true;
    } else {
      return false;
    }
  },
  isAndroid: function () {
    if (/android/i.test(navigator.userAgent)) { //这是android平台下浏览器
      return true;
    } else {
      return false;
    }
  },
  isIos: function () {
    if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) { //这是iOS平台下浏览器
      return true;
    } else {
      return false;
    }
  }
};
/**
 * 生成提货3天日期
 * t：2015年10月28日
 * o：$("ul")
 * n：生成天数，默认3天
 */
function createTime(t,n,weekdays,fmt){
  var dateList = [];
  var num=n || 3;
  console.log(t)
  weekdays = weekdays || false;
  var str=t.replace(/年/g,"/").replace(/月/g,"/").replace(/日/g,"");
  var tempDate = new Date(str);
  tempDate.setDate(tempDate.getDate()+1);
  if(weekdays) num += Math.floor((tempDate.getDay() + num)/7)*2;
  for(var i = 0; i < num; i++) {
    var day = tempDate.getDay();
    var date = new Date(tempDate).Format(fmt); //"yyyy-mm-dd"
    if(!weekdays || !(day===6 || day===0)){
      dateList.push(date);
    }
    tempDate.setDate(tempDate.getDate()+1);
  }
  return dateList;
}
function isObjEqual(obj1, obj2) {
  if (obj1 === obj2) {
    return true
  }
  else {
    const keys1 = Object.getOwnPropertyNames(obj1)
    const keys2 = Object.getOwnPropertyNames(obj2)
    if (keys1.length !== keys2.length) {
      return false
    }
    for (const key of keys1) {
      if (obj1[key] !== obj2[key]) {
        return false
      }
    }
    return true
  }
}

const Util = {
  browser : browser,
  isObjEqual : isObjEqual,
  createTime:createTime
}

export default Util;
