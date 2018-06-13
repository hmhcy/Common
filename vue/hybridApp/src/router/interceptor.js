/**
 * Created by Administrator on 2018/6/1.
 */

/**
 * 请求拦截器
 * @param request
 * @param next
 * @returns {*}
 */
function interceptor(request, next, router) {
  next(function(response) {
    if(response.status===401){
      localStorage.removeItem("userLogged");
      router.push({path: '/login',query: { redirect: router.currentRoute.fullPath }});
    }else{
      next();
    }
  })
};

export default interceptor;
