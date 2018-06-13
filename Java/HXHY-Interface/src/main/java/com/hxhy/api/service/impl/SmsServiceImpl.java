package com.hxhy.api.service.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.hxhy.api.service.SmsService;
import com.hxhy.config.util.DateUtils;

@Service
@ConfigurationProperties(prefix = "aliyun.msg")
public class SmsServiceImpl implements SmsService {
	// 产品名称:云通信短信API产品,开发者无需替换
	static final String product = "Dysmsapi";
	// 产品域名,开发者无需替换
	static final String domain = "dysmsapi.aliyuncs.com";

	// TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
	/*
	 * static final String accessKeyId = "LTAIXsTy51nWeJr5"; static final String
	 * accessKeySecret = "X8PgDCS2FFzxbrUWXv3s72fFRlFOkF";
	 */

	private static String accessKeyId;

	private static String accessKeySecret;

	private String signName;

	/**
	 *
	 * @param userPhone
	 *            (STRING 可选 电话)
	 * @param userCode
	 *            (STRING 可选 验证码)
	 * @param smsCode
	 *            (STRING 可选 验证类型 身份验证验证码:SMS_134125254 登录确认验证码:SMS_134125253
	 *            登录异常验证码 SMS_134125252 用户注册验证码:SMS_134125251 修改密码验证码:SMS_134125250
	 *            信息变更验证码 SMS_134125249
	 * @return boolean
	 * @throws ClientException
	 */

	public boolean sendSms(String userPhone, String userCode, String smsCode) throws ClientException {

		boolean boolMsg = false;
		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		
		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(userPhone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(signName);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(smsCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");

		request.setTemplateParam("{\"code\":" + userCode + "}");

		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		request.setOutId("yourOutId");
		try {
			// hint 此处可能会抛出异常，注意catch
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			// System.out.println("短信接口返回的数据----------------");
			// System.out.println("Code=" + sendSmsResponse.getCode());
			// System.out.println("Message=" + sendSmsResponse.getMessage());
			// System.out.println("RequestId=" + sendSmsResponse.getRequestId());
			// System.out.println("BizId=" + sendSmsResponse.getBizId());
			if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
				boolMsg = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return boolMsg;
	}

	public static QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象
		QuerySendDetailsRequest request = new QuerySendDetailsRequest();
		// 必填-号码
		request.setPhoneNumber("");
		// 可选-流水号
		request.setBizId(bizId);
		// 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
		request.setSendDate(DateUtils.getDays());
		// 必填-页大小
		request.setPageSize(10L);
		// 必填-当前页码从1开始计数
		request.setCurrentPage(1L);

		// hint 此处可能会抛出异常，注意catch
		QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

		return querySendDetailsResponse;
	}
	
	

	public String getAccessKeyId() {
		return accessKeyId;
	}


	public void setAccessKeyId(String accessKeyId) {
		SmsServiceImpl.accessKeyId = accessKeyId;
	}


	public String getAccessKeySecret() {
		return accessKeySecret;
	}


	public void setAccessKeySecret(String accessKeySecret) {
		SmsServiceImpl.accessKeySecret = accessKeySecret;
	}


	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}


	public static void main(String[] args) throws ClientException, InterruptedException {
		/*
		 * SmsServiceImpl ss = new SmsServiceImpl(); if (
		 * ss.sendSms("15280010373","13216","SMS_134125254")){
		 * System.out.println("--------成功--------"); }else{
		 * System.out.println("--------失败--------"); }
		 */

		// 发短信
		// SendSmsResponse response = sendSms("13850179698","12456","SMS_134125254");
		// System.out.println("短信接口返回的数据----------------");
		// System.out.println("Code=" + response.getCode());
		// System.out.println("Message=" + response.getMessage());
		// System.out.println("RequestId=" + response.getRequestId());
		// System.out.println("BizId=" + response.getBizId());
		//
		// Thread.sleep(3000L);
		//
		// //查明细
		// if(response.getCode() != null && response.getCode().equals("OK")) {
		// QuerySendDetailsResponse querySendDetailsResponse =
		// querySendDetails(response.getBizId());
		// System.out.println("短信明细查询接口返回数据----------------");
		// System.out.println("Code=" + querySendDetailsResponse.getCode());
		// System.out.println("Message=" + querySendDetailsResponse.getMessage());
		// int i = 0;
		// for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO :
		// querySendDetailsResponse.getSmsSendDetailDTOs()) {
		// System.out.println("SmsSendDetailDTO["+i+"]:");
		// System.out.println("Content=" + smsSendDetailDTO.getContent());
		// System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
		// System.out.println("OutId=" + smsSendDetailDTO.getOutId());
		// System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
		// System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
		// System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
		// System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
		// System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
		// }
		// System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
		// System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
		// }
	}
	// public static void main(String[] args) throws ClientException,
	// InterruptedException{
	// String retStr = "";
	// String strTable = false ? "1234567890" :
	// "1234567890abcdefghijkmnpqrstuvwxyz";
	// int len = strTable.length();
	// boolean bDone = true;
	// do {
	// retStr = "";
	// int count = 0;
	// for (int i = 0; i < 4; i++) {
	// double dblR = Math.random() * len;
	// int intR = (int) Math.floor(dblR);
	// char c = strTable.charAt(intR);
	// if (('0' <= c) && (c <= '9')) {
	// count++;
	// }
	// retStr += strTable.charAt(intR);
	// }
	// if (count >= 2) {
	// bDone = false;
	// }
	// } while (bDone);
	// System.out.println("=== " + retStr);
	//
	// }
}
