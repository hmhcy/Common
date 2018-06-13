//px单位换算rem单位
(function (doc, win) {
	var docEl = doc.documentElement,
		resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
		recalc = function () {
			var clientWidth = docEl.clientWidth;
			if (!clientWidth) return;
			if(clientWidth>=750){
				docEl.style.fontSize = '100px';
			}else{
				docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
			}
		};

	if (!doc.addEventListener) return;
	win.addEventListener(resizeEvt, recalc, false);
	doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);

function prepare () {

}

function already () {
  $('.loading').hide()
}

//
////点赞开关事件
//$(document).on('click' ,'.praiseClick',function(){
//  var praiseNum = $(this).html();
//  if($(this).attr('data-state') == 'on'){
//    $(this).removeClass('prabtnOn');
//	$(this).attr('data-state','off');
//	$(this).html(parseInt(praiseNum)-1);
//	$('.tipInfoCont').fadeIn(300);
//	$('.tipInfoCont p').html('取消关注');
//	setTimeout(function(){
//	  $('.tipInfoCont').fadeOut(300);
//	},2000);
//  }else{
//    $(this).addClass('prabtnOn');
//	$(this).attr('data-state','on');
//	$(this).html(parseInt(praiseNum)+1);
//	$('.tipInfoCont').fadeIn(300);
//	$('.tipInfoCont p').html('关注成功');
//	setTimeout(function(){
//	  $('.tipInfoCont').fadeOut(300);
//	},2000);
//  }
//})
//
////打开买入弹窗事件
//$(document).on('click' ,'.purchaseClick',function(){
//  $('.purchasePopup').show();
//})
////规格选择事件
//$(document).on('click' ,'.pNormLine .pNormBtn',function(){
//  $('.pNormLine .pNormBtn').removeClass('pNormOn');
//  $(this).addClass('pNormOn');
//})
////数量++
//var priceNum = 1;
//function numAdd(){
//	var num_add = parseInt($("#quantity").val())+priceNum;
//	if($("#quantity").val()==""){
//		num_add = priceNum;
//	}
//	$("#quantity").val(num_add);
//}
////数量--
//function numDec(){
//	var num_dec = parseInt($("#quantity").val())-priceNum;
//	if(num_dec==0){
//		num_dec = priceNum;
//	}
//	$("#quantity").val(num_dec);
//}
//
////关闭买入弹窗事件
//$(document).on('click' ,'.deleteRound, .purchasePopupBg',function(){
//  $('.purchasePopup').fadeOut(300);
//})
