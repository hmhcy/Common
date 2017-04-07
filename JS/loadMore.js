module.exports = {
  data () {
    return {
      list: [],
      page: 1,
      limit: 10,
      total: 0,
      lock:false, //一次只能发送一个请求,避免请求溢出
      scrollable:null, //如需要滚动到底部时自动加载数据,则指定可滚动的ui对象
      loadMoreTarget:null, //装载列表的ui对象,用于获取底部的距离
      positionState:true, //记录加载状态开关,如为true,则重载页面时自动恢复定位
      stateObject:{}
    }
  },
  created () {
    this.initList()
  },
  watch: {
    page: 'loadData'
  },
  methods: {
    /**
     * 初始化列表
     */
    initList () {
      this.stateObject = JSON.parse(sessionStorage.getItem(this.$route.path));
      this.page = 1;
      if(this.stateObject && this.stateObject.page) this.limit *= this.stateObject.page;
      this.list = [];
      this.loadData();
    },
    /**
     * 加载更多
     */
    loadMore () {
      if(!this.lock && this.total > this.page * this.limit){
        this.page++;
        this.limit = 10;
        this.lock = true;
      }
    },
    /**
     * 推送到list中 因为vue的监听特性 只能用push进行数据的添加 如果有特殊处理 通过传递一个filter来过滤数据
     * @param list
     * @param filter
     */
    pushToList (list, filter) {
      list.forEach((item) => {
      if (typeof filter === 'function') {
        this.list.push(filter(item))
      } else {
        this.list.push(item)
      }
      })
      this.lock = false;
      if(this.scrollable){
        this.$nextTick(this.scroll);
      }
    },
    /**
     * 监听滚轮事件,滑动加载更多数据
     */
    scroll (){
      var _self = this;
      if(_self.stateObject && _self.stateObject.top){
        $( _self.scrollable).scrollTop(_self.stateObject.top)
        sessionStorage.removeItem(this.$route.path)
      }
      $( _self.scrollable).scroll(function() {
        var target = $(_self.loadMoreTarget).children(":last-child");
        var offset = target.position().top;
        var elementHeight = target.height();
        console.log(offset)
        if(offset < elementHeight * 2){
          _self.loadMore();
        }
      });
    },
    /**
     * @overwrite
     * 加载数据方法 用到该mixin的都应该重写该方法 否则无法实现加载数据
     */
    loadData () {
      // 每个列表自己的获取数据的方法需要重写
    },
    /**
     * 离开页面是,记住页面浏览信息,以便重载页面时复原,name为当前页面路径
     * 使用vue-router中的deactivate钩子触发离开页面的事件,则页面路径为transition.from.path
     * @param name
     */
    onLeave (name) {
      sessionStorage.setItem(name,JSON.stringify({top:$(this.scrollable).scrollTop(),page:this.page}));
    }
  }
}
