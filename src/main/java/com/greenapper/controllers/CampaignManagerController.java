package com.greenapper.controllers;

import com.greenapper.dtos.ServerRequest;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.services.CookieService;
import com.greenapper.services.HttpRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/campaign-manager/")
public class CampaignManagerController {

	@Autowired
	private CookieService cookieService;

	@Autowired
	private HttpRequestService httpRequestService;

	@GetMapping("/password")
	public String getUpdatePasswordForm(final PasswordUpdateForm passwordUpdateForm) {
		return "campaign_manager/passwordUpdate";
	}

	@PatchMapping("/password")
	public String updatePassword(final PasswordUpdateForm passwordUpdateForm, final Errors errors) {
		final ServerRequest serverRequest = new ServerRequest();
		serverRequest.setMethod("PUT");

		final HashMap<String, String> requestParams = new HashMap<>();
		requestParams.put("Authorization", "Bearer " + cookieService.getCampaignManagerToken());
		requestParams.put("Content-Type", "application/json");
		serverRequest.setRequestParameters(requestParams);

		serverRequest.setRelativeUri("/campaign-manager/password");
		serverRequest.setSuccessRedirectUri("redirect:/campaign-manager/campaigns");
		serverRequest.setErrorRedirectUri("campaign_manager/passwordUpdate");

		return httpRequestService.sendAndHandleRequest(serverRequest, passwordUpdateForm, errors);
	}
}
