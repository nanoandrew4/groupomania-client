package com.greenapper.services.impl;

import com.greenapper.services.CookieService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DefaultCookieService implements CookieService {

	private String campaignManagerToken;

	@Override
	public String getCampaignManagerToken() {
		return campaignManagerToken;
	}

	@Override
	public void setCampaignManagerToken(String token) {
		this.campaignManagerToken = token;
	}
}
