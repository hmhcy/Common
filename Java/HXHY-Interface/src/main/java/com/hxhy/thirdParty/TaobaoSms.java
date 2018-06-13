/*package com.hxhy.thirdParty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class TaobaoSms {
	// 正式环境
	// public static String URL = PropConfig.getPropValue("TAOBAO_SMS_URL");
	public static String URL = "http://gw.api.taobao.com/router/rest";
	// public static String URL = "https://eco.taobao.com/router/rest";

	// 沙箱环境
	// public static String URL = "http://gw.api.tbsandbox.com/router/rest";
	// public static String URL = "https://gw.api.tbsandbox.com/router/rest";

	// appkey
	public static String APPKEY = "23259376";
	// public static String APPKEY = PropConfig.getPropValue("TAOBAO_APPKEY");

	// secret
	public static String SECRET = "4cce3796ea9b2f0069ab593d5abe3ea3";
	// public static String SECRET = PropConfig.getPropValue("TAOBAO_SECRET");
	static Logger logger = LogManager.getLogger(TaobaoSms.class);

	public static boolean sendMessage(String telephone, JSONObject dataJson) {

		// 解析JSON 
		String code = dataJson.getString("code");
		String params = "";
		try {// 避免有的json对象中没有params参数。
			params = dataJson.getString("params");
		} catch (Exception e) {
			params = "";
			logger.error("调用无参短信模板：" + code + "." + e.getMessage());
		}
		String sessionKey = "";

		TaobaoClient client = new DefaultTaobaoClient(TaobaoSms.URL, TaobaoSms.APPKEY, TaobaoSms.SECRET);
		logger.info("Url：" + TaobaoSms.URL);
		logger.info("APPKEY：" + TaobaoSms.APPKEY);
		logger.info("SECRET：" + TaobaoSms.SECRET);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();

		// 可选,公共回传参数，在“消息返回”中会透传回该参数；
		// 举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，用户可以根据该会员ID识别是哪位会员使用了你的应用
		// req.setExtend("123456");

		// 必须,短信类型，传入值请填写normal
		req.setSmsType("normal");

		// 必须,短信签名，传入的短信签名必须是在阿里大鱼“管理中心-短信签名管理”中的可用签名
		// req.setSmsFreeSignName("集珍坊");
		req.setSmsFreeSignName("集珍坊");
		// 短信模板变量，传参规则{"key"："value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开，示例：{"name":"xiaoming","code":"1234"}
		// req.setSmsParam("{\"AckNum\":\"123456\"}");
		if (!StringUtils.isEmpty(params.trim())) {
			req.setSmsParam(params);
		}

		// 可选,短信接收号码，11位手机号码，不能加0或+86
		req.setRecNum(telephone);
		// 短信模板ID，传入的模板必须是在阿里大鱼阿里大鱼“管理中心-短信模板管理”中的可用模板
		req.setSmsTemplateCode(code);
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req, sessionKey);

			// 解析回传结果。。。。
			--成功
			{"alibaba_aliqin_fc_sms_num_send_response":{"result":{"model":"100191546099^1100157019479","success":true},"request_id":"13ozuxm2pyqh2"}}
			
			--失败
			{"alibaba_aliqin_fc_sms_num_send_response":{"result":{"msg":"模板配置未找到, smsCode:SMS_99645048, partnerId: 99101405015","success":false},"request_id":"13oz93w3idv1t"}}
			JSONObject repJson = JSONObject.parseObject(rsp.getBody());
			logger.error("短信回调返回：" + rsp.getBody());
			JSONObject repObject = repJson.getJSONObject("alibaba_aliqin_fc_sms_num_send_response");
			JSONObject resultObject = repObject.getJSONObject("result");
			// logger.info(repJson.toString());
			return resultObject.getBoolean("success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		*//*** 淘宝短信平台所需参数 *//*
		JSONObject jsonMessage = new JSONObject();
		jsonMessage.put("code", "SMS_72950065");
		JSONObject params = new JSONObject();
		params.put("product", "[lot007]雨翼作芙蓉石霜露清盈呈晓艳薄意章" );
		jsonMessage.put("params", params);
		TaobaoSms.sendMessage("18094007696", jsonMessage);
	}
}
*/