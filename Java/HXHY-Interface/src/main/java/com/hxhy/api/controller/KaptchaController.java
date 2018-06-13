package com.hxhy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.kaptcha.Kaptcha;
import com.baomidou.kaptcha.exception.KaptchaNotFoundException;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/kaptcha")
@Api(value = "验证码", tags = { "验证码" })
public class KaptchaController {

  @Autowired
  private Kaptcha kaptcha;

  @GetMapping("/render")
  public void render() {
    kaptcha.render();
  }

  @PostMapping("/valid")
  public void validDefaultTime(@RequestParam String code) {
    //default timeout 900 seconds
	try {
		 kaptcha.validate(code);
	}catch(NullPointerException np) {
		throw new KaptchaNotFoundException();
	}
   
  }

  @PostMapping("/validForOneMin")
  public void validWithTime(@RequestParam String code) {
    kaptcha.validate(code, 60);
  }

}