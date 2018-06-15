/**
 * Created by Administrator on 2018/6/14.
 */

function getStyle(el, styleProp) {
  var value, defaultView = (el.ownerDocument || document).defaultView;
  // W3C standard way:
  if (defaultView && defaultView.getComputedStyle) {
    // sanitize property name to css notation
    // (hypen separated words eg. font-Size)
    styleProp = styleProp.replace(/([A-Z])/g, "-$1").toLowerCase();
    return defaultView.getComputedStyle(el, null).getPropertyValue(styleProp);
  }
}

const install = function (Vue) {
  Vue.directive('focus', {
    // When the bound element is inserted into the DOM...
    inserted: function (el) {
      // Focus the element
      el.focus()
    }
  })

  var css = document.createElement('style');
  css.type = 'text/css';
  Vue.directive('sticky',{
    bind(el){
      var styles = '.is-fixed { top: 1rem ;right: 0;left: 0; position: fixed; z-index: 1000;}';
      if (css.styleSheet) css.styleSheet.cssText = styles;
      else css.innerText = styles
      document.getElementsByTagName("head")[0].appendChild(css);
    },
    inserted(el,binding,vnode) {
      var topH = document.querySelector("header").clientHeight
      var margin = parseInt(getStyle(el,"marginTop")) +  parseInt(getStyle(el,"paddingTop"));
      console.log(margin)
      var hdr = el.offsetTop - topH
      var stickyElement =el.firstElementChild;
      if(binding.value){
        stickyElement = document.querySelector("."+binding.value);
      }
      $(".page").scroll(function() {
        if( $(this).scrollTop() > hdr ) {
         if(!$(stickyElement).hasClass("is-fixed")){
           $(stickyElement).addClass("is-fixed")
           stickyElement.style.top = (topH-1)+"px";
         }
          //$(".van-tabs__wrap").addClass("is-fixed")
        } else {
          if($(stickyElement).hasClass("is-fixed")){
            $(stickyElement).removeClass("is-fixed")
            stickyElement.style.top = 0;
          }
          //$(".van-tabs__wrap").removeClass("is-fixed")
        }
      });
    },
    unbind(){
      document.getElementsByTagName("head")[0].removeChild(css);
    }
  })
}
const VueDirectives = { install }

export default VueDirectives

