package com.hxhy.api.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hxhy.api.responseEntity.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "order")
@Api(value = "订单操作", tags = { "订单操作" })
public class OrderController {

	@ApiOperation(value = "下单")
	@RequestMapping(value = "/placeOrder", method = RequestMethod.PUT,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResultBean<String> register(@RequestParam(name="用户") String user) {
		ResultBean<String> result = new ResultBean<String>();
		result.setMsg("成功");
		return result;
	}

}
