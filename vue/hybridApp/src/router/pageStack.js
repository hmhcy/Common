/**
 * Created by Administrator on 2018/5/8.
 */

function contains(target,obj){
  var i = target.length;
  while(i--){
    if(target[i]===obj)return true
  }
  return false
}

const nextDirection = (direction) => {
  let el = document.getElementById('app')
  if (el) el.setAttribute('transition-direction', direction)
}

window.onpopstate = function(event) {
  nextDirection('back')
};

const deviceTransition = (on) => {
  // 判断设备类型
  let el = $("#app")
  if(!el) return false;
  if (on){
    console.log(/-device(\s|$)/.test(el.attr("class")))
    if(/-device(\s|$)/.test(el.attr("class"))) return false;

    if (/iPad|iPhone|iPod/.test(navigator.userAgent)) {
      el.addClass('ios-device')
    } else {
      el.addClass('android-device')
    }
  }else{
    console.log($('div[class*="-device"]'))
    el[0].className = el[0].className.replace(/(ios|android)-device(\s|$)/, '');
  }
}

let pageStack =  Object.create(Array.prototype);
let pageStack_names =  Object.create(Array.prototype);
const Pagestack = {
  use (router) {
    router['_push'] = router['push']
    router['_pop'] = router['back']
    router['_pages'] = pageStack

    router.beforeEach((to, from, next) => {
      /*if(to.name!=="login"){
        pageStack.extends(to.matched.map(m => m.components.default))
      }*/
      //pageStack.extends(to.matched.map(m => m.components.default))
    if(!from.name && pageStack.length < 1){
      var paths = sessionStorage.getItem("PAGE_NAV") ? sessionStorage.getItem("PAGE_NAV").split(",") : []
      if(paths.length > 0){
        for(var i = 0,j=paths.length;i < j;i++){
          pageStack.extends(router.getMatchedComponents({name:paths[i]})[0])
        }
      }else{
        pageStack.extends(router.getMatchedComponents({name:"index"})[0])
      }
    }

    if(to.meta.requireLogin!==undefined && to.meta.requireLogin){
      let assesToken = localStorage.getItem("userLogged");
      if (!assesToken) {
        next({
          path: '/login',
          query: { redirect: to.fullPath }
        })
      }
    }
    //pageStack.extends(to.matched[0].components.default);
    pageStack.extends(to.matched.map(m => m.components.default))
    //pageStack_names.extends()
    sessionStorage.setItem("PAGE_NAV",pageStack.map(m => m.name).join(","))
    next()
  })
// 重写路由跳转方法，设置跳转类型后跳转
    router.forward = router['push'] = (target) => {
      nextDirection('forward')
      router['_prevDirection'] = "forward";
      //router['_path'][target.name] = router.getMatchedComponents(target)[0]
      setTimeout(() => { router['_push'](target)})
    }

// 重写路由返回方法，设置跳转类型后跳转到上一页
     router.back = () => {
      nextDirection('back')
      router['_prevDirection'] = "back";
      var target = pageStack[pageStack.length - 2] ? pageStack[pageStack.length - 2] .name : "";
      if(target){
        setTimeout(() => { router['_push']({name:target}) })
      }else{
        setTimeout(() => { router.go(-1)})
      }
    }
  }
}

export default Pagestack;
