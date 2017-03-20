/**
 * 设置对象属性featureSet 的设值和获取值的方法
 */
function Subobj() {
    var featureSet = [];//featureSet空数组对象
    Object.defineProperty(this, 'featureSet', { //定义featureSet 属性
        get: function() {//获取featureSet
            return featureSet;
        },
        set: function(value) { //featureSet赋值
            featureSet.push(value);
        }
    });
}
/**
 * 对象属性定义 ：包含 objs，ups，rules
 */
function Objset(objs){
    this.objs = objs; //属性对象
    this.ups = {}; //属性树
    this.rules={}; //规则对象
}

/**
 * 构建属性树
 * @param pKey 父键
 * @param cKey 子健
 * @param val 设置的value
 */
Objset.prototype.pushPros = function(pKey,cKey,val) {
    if (pKey === "") {//父键为""
        if (typeof this.ups[cKey] === "undefined") {//且子健未定义
            this.ups[cKey] = val;
        }
    } else {//父键不为空
        if (typeof this.ups[pKey] !== "undefined") {//父键在当前数据对象中已经定义
            if (cKey != "valArr") {//子健不为数组变量
                this.ups[pKey][cKey] = val;
            } else {//子健为“valArr”时
                if (typeof this.ups[pKey][cKey] === "undefined") {//如果当前数组的值未定义，置空当前数组
                    this.ups[pKey][cKey] = []; //置空数组
                }
                if (this.ups[pKey][cKey].indexOf(val) < 0) {//如果当前数组中不存在当前值，则往当前数组中添加当前变量值
                    this.ups[pKey][cKey].push(val);//往当前数组中添加当前变量值
                }
            }
        }
    }
}

/**
 * 创建规则:商品可选与不可选
 */
Objset.prototype.createRules = function(){
    var ups = this.ups;
    var rules = this.rules;
    for(var key in ups){
        rules[key]={};
        ups[key].valArr.forEach(function(val){//遍历当前变量数组
            rules[key][val]={};
            for(var rule in ups){
                if(key!==rule){
                    rules[key][val][rule]=ups[rule].valArr.slice(0);
                }
            }
        })
    }
    this.setRules(rules,this.objs);
};

/**
 * 创建规则:商品可选与不可选 ----子方法
 * @param rules  规则
 * @param objs 设置的对象
 */
Objset.prototype.setRules = function(rules,objs){
    var _self = this,temp="";
    for(var rule in rules){
        if(typeof rules[rule].length === "undefined"){
            $.each(rules[rule], function(index, prop) {
                temp = objs.filter(function(value){
                    return value.featureSet[rule]===index;
                })
                return  _self.setRules(prop,temp);
            });
            $.each(rules[rule], function(index, prop) {
                $.each(prop, function(i, p){
                    if(p.length<1){
                        delete prop[i];
                    }
                })
                if ($.isEmptyObject(prop)){
                    delete rules[rule][index];
                }
            });
        }else{
            var length =  rules[rule].length;
            for(var i = length -1; i >= 0 ; i--){
                temp = objs.filter(function(value){
                    if(value.featureSet[rule]===rules[rule][i]){
                        rules[rule].splice(i,1);
                    }
                    return value.featureSet[rule]===rules[rule][i];
                })
            }
        }
    }
}

exports.Subobj = Subobj;
exports.Objset = Objset;
