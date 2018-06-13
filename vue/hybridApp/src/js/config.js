/**
 * 配置前端请求路径
 */
let url;
if (process.env.NODE_ENV == 'development') {
  //url = 'http://develop.artjzf.com'//本地
  //url = "http://examjzf.jizhenfang.com"
  url = "http://192.168.1.109:8080/hxhyapi"
} else if (process.env.NODE_ENV == 'production') {
  url = window.location.protocol + '//' + window.location.host//服务器
}
const ctx = url;
const localhost = window.location.protocol + "//" + window.location.host + '/#'//配置前端路径
window.ctx = ctx;
window.localhost = localhost;
