package com.hxhy.config.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class ConfigureProperties {

    private String headUrl;
    private String aesKey;
    private String aesIv;
    private String domainAccesable;
    
	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	public String getAesKey() {
		return aesKey;
	}
	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}
	public String getAesIv() {
		return aesIv;
	}
	public void setAesIv(String aesIv) {
		this.aesIv = aesIv;
	}
	public String getDomainAccesable() {
		return domainAccesable;
	}
	public void setDomainAccesable(String domainAccesable) {
		this.domainAccesable = domainAccesable;
	}
    
    //getters and setters

}