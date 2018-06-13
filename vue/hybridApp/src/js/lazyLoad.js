/**
 * Created by Administrator on 2017/10/19.
 */

var options = {
  root:null,
  rootMargin: "0px",
  threshold: 0.3
};
var observer = null;
var processList = [];

if('IntersectionObserver' in window &&
  'IntersectionObserverEntry' in window &&
  'intersectionRatio' in window.IntersectionObserverEntry.prototype){

  observer = new IntersectionObserver(function(entries) {
    entries.forEach(function (entry) {
      if (entry.intersectionRatio >= 0) {
        processList.splice(processList.indexOf(entry),1);
        let src = entry.target.getAttribute("vsrc");
        if ("IMG" === entry.target.tagName && src && !entry.target.src) {
          entry.target.src = src;
        }
        entry.target.removeAttribute("vsrc");
        observer.unobserve(entry.target);
      }
    });
  }, options);
}

import Vue from 'vue';

const lazyImg = Vue.extend({
  template: '<img/>',
  props:["src","root"],
  mounted(){
    this.$el.addEventListener("error",function(){
      // this.src = ctx + '/static/images/loadFailure.png';
      this.src = 'http://app2.artjzf.com/static/images/loadFailure.png';
    })
    if(!('IntersectionObserver' in window &&
      'IntersectionObserverEntry' in window &&
      'intersectionRatio' in window.IntersectionObserverEntry.prototype)){
      this.$el.setAttribute('src',this.src);
      return false;
    }
    this.$el.setAttribute('vsrc',this.src);
    processList.push(this.$el);
    observer.observe(this.$el)
  }
})
export default lazyImg;
