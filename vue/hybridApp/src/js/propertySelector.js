/**
 * Created by Administrator on 2017/7/3.
 */
function Subobj () {
  var featureSet = [];
  Object.defineProperty(this, 'featureSet', {
    get: function () {
      return featureSet;
    },
    set: function (value) {
      featureSet.push(value);
    }
  });
}
function Objset (objs) {
  this.objs = objs;
  this.ups = {};
  this.rules = {};
}
//  Objset.prototype.ups = {};
Objset.prototype.pushPros = function (pKey,cKey,val) {
  if (pKey === "") {
    if (typeof this.ups[cKey] === "undefined") {
      this.ups[cKey] = val;
    }
  } else {
    if (typeof this.ups[pKey] !== "undefined") {
      if (cKey !== "valArr") {
        this.ups[pKey][cKey] = val;
      } else {
        if (typeof this.ups[pKey][cKey] === "undefined") {
          this.ups[pKey][cKey] = [];
        }
        if (this.ups[pKey][cKey].indexOf(val) < 0) {
          this.ups[pKey][cKey].push(val);
        }
      }
    }
  }
}
//  Objset.prototype.rules = {};
Objset.prototype.createRules = function () {
  var ups = this.ups;
  var rules = this.rules;
  for (var key in ups) {
    rules[key] = {};
    ups[key].valArr.forEach(function (val) {
      rules[key][val] = {};
      for (var rule in ups) {
        if (key !== rule) {
          rules[key][val][rule] = ups[rule].valArr.slice(0);
        }
      }
    })
  }
  this.setRules(rules,this.objs);
};
Objset.prototype.setRules = function (rules, objs) {
  var _self = this;
  var temp = "";
  for (var rule in rules) {
    if (typeof rules[rule].length === "undefined") {
      $.each(rules[rule], function (index, prop) {
        temp = objs.filter(function (value) {
          return value.featureSet[rule] === index;
        })
        return _self.setRules(prop, temp);
      });
      $.each(rules[rule], function (index, prop) {
        $.each(prop, function (i, p) {
          if (p.length < 1) {
            delete prop[i];
          }
        })
        if ($.isEmptyObject(prop)) {
          delete rules[rule][index];
        }
      });
    } else {
      var length = rules[rule].length;
      for (var i = length - 1; i >= 0; i--) {
        temp = objs.filter(function (value) {
          if (value.featureSet[rule] === rules[rule][i]) {
            rules[rule].splice(i,1);
          }
          return value.featureSet[rule] === rules[rule][i];
        })
      }
    }
  }
}

exports.Subobj = Subobj;
exports.Objset = Objset;
