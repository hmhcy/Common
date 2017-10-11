/**
 * Created by Administrator on 2017/7/24.
 */
function PageStack(){
  this.breadscrumb = [];
  this.scrollRestoration = {};
  this.next = null;

  Object.defineProperty(this.breadscrumb,"set",{
    configurable: false,
    enumerable: false,
    writable: false,
    value: function (val) {
      if(typeof val.length === "number" && val.length > 0){
        var _self = this;
        val.forEach(function(e){
          _self.push(e);
        })
      }
    }
  })

  Object.defineProperty(this.breadscrumb, "add", {
    configurable: false,
    enumerable: false,
    writable: false,
    value: function (val) {
      if(val == "#!/index") val = "#!/"
      var i = 0,l = this.length,duplicate=false;
      for(i,l;i<l;i++){
        var path = this[i],mainpath = path.split("?")[0],sp = val.split("?")[0];
        if(mainpath==sp){
          duplicate=true
          break;
        }
      }
      if(duplicate){
        this.splice(i,l-i,val);
      } else {
        this.push(val)
      }
      window.history.replaceState(this, "", val);
    }
  });

  Object.defineProperty(this.breadscrumb, "remove", {
    configurable: false,
    enumerable: false,
    writable: false,
    value: function (val) {
      if(val == "#!/index") val = "#!/"
      var i = 0,l = this.length;
      for(i,l;i<l;i++){
        var path = this[i],mainpath = path.split("?")[0],sp = val.split("?")[0];
        if(mainpath==sp){
          break;
        }
      }
      this.splice(i,1);
      window.history.replaceState(this, "", window.location.hash);
    }
  });

}
var ps = new PageStack();

window.onload = function(){
  //alert("onload")
  if(sessionStorage.getItem("breadscrumb")){
    ps.breadscrumb.set(sessionStorage.getItem("breadscrumb").split(","));
    sessionStorage.removeItem("breadscrumb")
    return false
  }
  ps.breadscrumb.push("#!/")
  if(window.location.hash !== "#!/" && window.location.hash.indexOf("jzfLogin")<0){ps.breadscrumb.push(window.location.hash)}
  window.history.pushState(ps.breadscrumb, "","/"+window.location.hash);
};
window.onunload = function(){
  alert("unload")
  sessionStorage.setItem("breadscrumb",ps.breadscrumb.join(","));
  return "unload"
}
/**
 * if newUrl in breadscrumb
 * && oldUrl in breascrumb
 * && indexOf newUrl < indexOf oldUrl
 * then isbackward
 * else isforward
 *
 * if isforward then add to path
 * elsif isbackward then pop oldUrl from path
 */
/*
 e.newURL
 e.oldURL
 */
window.onhashchange = function(e){
  //alert(window.location.href)
  ps.next=null;

  if(/(https|ios|android)/.test(window.location.hash)) return false
  var pathName = e.newURL.split("#")[1].replace("!","");
  if(ps.scrollRestoration[pathName]){
   var inter = setInterval(function() {
     if($("article.main").length > 0 && document.querySelector("article.main").scrollHeight > ps.scrollRestoration[pathName] ){
       $("article.main").animate({"scrollTop":ps.scrollRestoration[pathName]},300);
       clearInterval(inter);
     }
    },200);
    setTimeout(function(){
      clearInterval(inter);
    },1000)
  }
  ps.breadscrumb.add(window.location.hash);
  return false;
};

/*window.onpopstate = function(){
  if(ps.next != null){
    ps.next();
    return false
  }
  console.log("pop")
  console.log(event.state)
  if(event.state!=null) {
    console.log(ps.breadscrumb)
    console.log(event.state)
    if (typeof event.state.length == "number") {
      var l = ps.breadscrumb.length, diff = l - event.state.length;
      var track = ps.breadscrumb;
      if (diff < 0) {
        track = event.state;
        l = event.state.length;
      }
      window.location.hash = track[l - 2] || "#!/";
      return false;
    }
  }
}*/

var goOri = window.history.go.bind(window);
window.history.go = function(number){
  var l =ps.breadscrumb.length-1
  if(ps.breadscrumb[l+number]){
    window.location.hash = ps.breadscrumb[l+number]
  }else{
    //history.go(number);
    //goOri(number);
    window.history.back();
  }
}

const Breadscrumb = {
  use (router) {
    router._scrollRestoration = {
      save(pathName){
        if(pathName === undefined) return false;
        ps.scrollRestoration[pathName] = $("article.main").scrollTop();
      }
    }
    router._breadscrumb = {
      push (path,cb){
        ps.breadscrumb.add(path);
        //window.location.hash=path;
        if(typeof cb === "function"){
          ps.next = cb;
        }
      },
      pop (path,cb){
        ps.breadscrumb.remove(path);
        //window.location.hash=path;
        if(typeof cb === "function"){
          ps.next = cb;
        }
      },
      save(){
        sessionStorage.setItem("breadscrumb",ps.breadscrumb.join(","));
      },
      read(){
        if(sessionStorage.getItem("breadscrumb")){
          ps.breadscrumb.set(sessionStorage.getItem("breadscrumb").split(","));
          sessionStorage.removeItem("breadscrumb")
        }
      }
    }
/*    window.onpopstate=function(){
      console.log(router)
    }*/
  }
}



export default Breadscrumb;
/**
 * Created by Administrator on 2017/7/26.
 */
