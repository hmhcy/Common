<template>
  <!--确认购买弹窗-->
  <div class="alertConfirmBuy" transition="SlideTopBottom">
    <ul class="goodsListRow">
      <li class="follow">
        <div class="box">
          <a href="javascript:;" class="img">
            <img :src="detail.headUrl + detail.commodityImg" alt="" />
          </a>
          <div class="info">
            <a href="javascript:;">
              <h3>{{detail.commodityName}}</h3>
              <strong >{{price}}</strong>
            </a>
            <span class="num">数量：{{stockCount}}</span>
          </div>
        </div>
      </li>
    </ul>
    <div class="scrollBox">
      <template v-for="v in subObjs.ups">
        <h4>{{v.name}}</h4>
        <ul class="type">
          <li v-for="s in v.valArr" :class="[selected(v.code,s)?'select':'',!available(v.code,s)? 'no':'']"  @click.prevent="select($event,v.code,s)">
            <a href="javascript:;">{{s}}</a>
          </li>
        </ul>
      </template>
      <h4>购买数量</h4>
      <div class="number">
        <div :class="['reduce', {select: num===1}]" @click="num > 1? num-- :num"><span></span></div>
        <input type="number" v-model="num" readonly="readonly"/>
        <div :class="['add', {select: stockCountAv <= num}]" @click="num < stockCountAv ? num++ :num"><span></span></div>
      </div>
    </div>
    <div class="btn">
      <a href="javascript:;" v-if="carorclear != 2" @click="toClear()">确认</a><!--进入下单页 -->
      <a href="javascript:;" v-if="carorclear == 2" @click="addCar()">确认</a><!--加入购物车 -->
    </div>
    <a href="javascript:;" class="exit" @click="exit"></a>
  </div>
</template>
<script>
  import {Subobj,Objset} from './propertySelector';
  import Vue from 'vue'
  export default{
    data(){
      return{
        subObjs:[],//商品对象集合
        currentItem:"",//当前属性组
        availableItems:[],//可选的属性组
        selectPath:{}, //保存已经选中的属性
        num:1, //购买数量
        price:"",//价格
        priceRange:"",//价格区间
        stockCountAv:0,//购买上限
        stockCount:0,//库存量
        userInerv:false,// ？？
      }
    },
    props: ["detail","carorclear","showControl"],
    ready:function(){
    },
    methods:{
      init(){
        var i= 0;
        var priAtt = this.detail.showAttr;
        var hideAtt = this.detail.hideAttr;
        this.stockCount = this.detail.stockCount;
        var subObjs = [],subObj,objset = new Objset(subObjs);
        for(i;i<priAtt.length;i++){
          subObj = new Subobj();
          var j=0;
          for(j;j<priAtt[i].length;j++){
            var attrs = priAtt[i][j];
            subObj.featureSet[attrs.code]=attrs.value;//设置组编码
            subObj.featureSet["group"] = attrs.group;//设置组id
            subObj.featureSet["price"] = hideAtt[attrs.group].substantialPrice || hideAtt[attrs.group].price;//设置组价格
            subObj.featureSet["stockCount"] = hideAtt[attrs.group].stockCount;//设置组库存
            subObj.featureSet["commodityId"] = hideAtt[attrs.group].commodityId;//设置商品id
            if(attrs.value){//如果值存在的话，往对象集合中加入当前组数据
              objset.pushPros("",attrs.code,{});//设置组编码,父键为空
              objset.pushPros(attrs.code,"code",attrs.code);//设置数据编码
              objset.pushPros(attrs.code,"name",attrs.name);//设置数据名称
              objset.pushPros(attrs.code,"valArr",attrs.value);//设置数据值
            }
          }
          var _self = this;
          subObjs.push(subObj);//将商品塞入商品集合
        }
//        objset.objs = subObjs; //冗余代码
        this.subObjs = objset;
        Object.keys(objset.ups).forEach(function(key){ //遍历商品对象的键
          if(objset.ups[key].valArr.length < 2){//如果属性组的值小于二，当前属性默认选中
            Vue.set(_self.selectPath,key,objset.ups[key].valArr[0]);
            _self.filterItem();//过滤当前组
          }else{//否则当前选中的属性为空对象，既不选中任何属性
            _self.selectPath = {};
          }
        });
        objset.createRules();// 详见上面方法；待解析
        this.priceRange = this.price =this.getPriceRange(subObjs); //得出商品的价格范围
        this.showControl = true;//是否展示选择商品弹窗，由父级页面传入
      },
      select:function(el,prop,val){//选择商品属性，el:原属；prop：属性；val:值
        if($(el.target).parent("li").hasClass("no")) return false;//如果当前商品不可选，则结束
        if($(el.target).parent("li").hasClass("select")){//如果当前属性值已经选中，则取消选中
          Vue.delete(this.selectPath, prop);//将当前属性从已经选中的属性组中清除
        }else{// 否则将当前属性值选中，
          Vue.set(this.selectPath,prop,val);//放入选中商品属性组中
        }
        this.userInerv = true;
        this.filterItem();//过滤属性组
      },
      available:function(prop,val){//计算当前属性值是否可选
        if(this.subObjs.objs.filter(function(value){//如果商品对象中存在当前属性值的商品，且库存为空，则将该商品剔除
            return value.featureSet[prop]===val && value.featureSet["stockCount"]<1;
          }).length > 0) return false;//如果可选的商品数量

        if(this.selectPath.length<0) return true;
        var _self = this,able=true;
        $.each(this.selectPath,function(i,p){
          if(_self.subObjs.rules[i]!==undefined && _self.subObjs.rules[i][p]!==undefined
            &&_self.subObjs.rules[i][p][prop]!==undefined && _self.subObjs.rules[i][p][prop].indexOf(val)>-1) {
            able = false;
            return false;
          }
        })
        return able;
      },
      selected:function(prop,val){ //已经选择的值
        return this.selectPath[prop]==val;
      },
      getPriceRange:function(objs){//获取商品价格区间
        if(Object.keys(objs).length < 1) return;//如果可选的商品数量为0，直接中断执行
        objs.sort(function(a, b){//从小到大排序
          return a.featureSet.price-b.featureSet.price
        });
        if(Object.keys(objs).length>1 && objs[0].featureSet.price < objs[Object.keys(objs).length-1].featureSet.price){//有多个商品时，返回价格区间
          return objs[0].featureSet.price + "~" + objs[Object.keys(objs).length-1].featureSet.price;
        }else{//否则返回价格
          return objs[0].featureSet.price;
        }
      },
      filterItem:function(){ //根据已选的属性，得到可选的商品
        var _self = this,temp=this.availableItems=[];

        if(Object.keys(this.selectPath).length<1){//如果选中的属性为空，则
          this.price = this.priceRange;//设置价格为价格区间
          this.stockCount = this.detail.stockCount;//设置库存为当前商品库存
          return;//结束方法
        }
        Object.keys(this.selectPath).forEach(function(prop){
          temp = _self.availableItems.length>0?_self.availableItems:_self.subObjs.objs;
          _self.availableItems = temp.filter(function(value){
            return value.featureSet[prop]===_self.selectPath[prop];
          });
        })
        this.price =_self.getPriceRange(_self.availableItems);
        this.stockCount = _self.availableItems.reduce( function(a, b){
          return a + parseInt(b.featureSet.stockCount);
        }, 0);
      },
      validate:function(){
        var msg = [];
        //if(this.availableItems.length<1)return false;
        var that = this;
        if(this.availableItems.length<1 || Object.keys(this.selectPath).length < Object.keys(this.subObjs.ups).length){
          Object.keys(this.subObjs.ups).forEach(function(key){
            if(that.selectPath[key]===undefined){
              msg.push(that.subObjs.ups[key].name);
            }
          });
          mAlaert.run({
            id : "mAlaert",
            content : "请选择" + msg.join(","),
            time : 1000
          });
          return false;
        };
        return true;
      },
      addCar : function() {//加入购物车调用的方法
        if(!this.validate()) return;
        var group = this.availableItems[0].featureSet.group;
        this.$http.post(//添加购物车数量
          ctx  + "/jzf/shopCar/addCar.do",
          {
            count : this.num, //添加的数量
            commodityId : this.detail.commodityId,//商品id
            group : group,//商品组id
            version : 1,//接口版本
            userToken : $.cookie("userToken")
          }
        ).then(
          function (response) {
            var c = response.data.resultMsg;
            mAlaert.run({
              id : "mAlaert",
              content : c,
              time : 1000
            });
            if(response.data.resultCode == 0){
              var _this = this;
              setTimeout(function() {
                _this.$parent.showConfirmBuy=false;
                if(_this.userInerv){
                  _this.selectPath = {};
                }
                _this.num = 1;
                $(".opacity").hide();
              },1500);
            }
          }
        );
      },
      toClear : function() {//进入下单页
        if(!this.validate()) return;
        var commodityList = new Array();
        var group = this.availableItems[0].featureSet.group;
        var commodity = {commodityId : this.detail.commodityId, number : this.num, group : group};
        commodityList[0] = commodity;
        $(".opacity").hide();
        this.$route.router.go({path: '/jzfinOrderClearing', query: {clStr: $.base64.encode(JSON.stringify(commodityList))}});
      },
      exit : function() {//退出选择
        this.$parent.showConfirmBuy = false;
        $(".opacity").hide();
        //this.resetData();
      },
    },
    watch:{
      detail :function(val){//监控商品详情
        if('string' === typeof val){//如果只有商品id，则先获取商品详情信息
          this.$http.post(
            ctx  + "/jzf/commodity/getCommodity.do",
            {
              commodityId : val,
              userToken: $.cookie("userToken"),
              version : 1
            }
          ).then(
            function (response) {
              this.detail = response.data.resultObj;//商品详情
            });
        }else if('object' === typeof val){//否则直接赋值
          this.init();
        }
      },
      'selectPath':function(val){//计算库存上限
        //this.filterItem();
        if(Object.keys(val).length === Object.keys(this.subObjs.ups).length){
          this.stockCountAv = this.stockCount;
        }else{
          this.stockCountAv = 0;
          this.num = 1;
        }
      }
    }
  }
</script>
