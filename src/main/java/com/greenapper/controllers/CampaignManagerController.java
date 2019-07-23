package com.greenapper.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.greenapper.dtos.ServerRequest;
import com.greenapper.dtos.ServerResponse;
import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.services.CookieService;
import com.greenapper.services.HttpRequestHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.HashMap;
import java.util.List;

@Controller
public class CampaignManagerController {

	@Autowired
	private CookieService cookieService;

	@Autowired
	private HttpRequestHandlerService httpRequestHandlerService;

	private final static String ROOT_URI = "/campaign-manager";

	public final static String PASSWORD_UPDATE_URI = ROOT_URI + "/password";

	public final static String PASSWORD_UPDATE_FORM = "campaign_manager/passwordUpdate";

	public final static String CAMPAIGNS_OVERVIEW_URI = ROOT_URI + "/campaigns";

	public final static String CAMPAIGNS_OVERVIEW_FORM = "campaign_manager/campaignsOverview";

	public final static String PASSWORD_UPDATE_SUCCESS_REDIRECT = "redirect:" + CAMPAIGNS_OVERVIEW_URI;

	@GetMapping(PASSWORD_UPDATE_URI)
	public String getUpdatePasswordForm(final PasswordUpdateForm passwordUpdateForm) {
		return PASSWORD_UPDATE_FORM;
	}

	@PutMapping(PASSWORD_UPDATE_URI)
	public String updatePassword(final PasswordUpdateForm passwordUpdateForm, final Errors errors) {
		final ServerRequest serverRequest = new ServerRequest();
		serverRequest.setRelativeUri("/campaign-manager/password");
		serverRequest.setMethod("PUT");
		serverRequest.setSuccessRedirectUri(PASSWORD_UPDATE_SUCCESS_REDIRECT);
		serverRequest.setErrorRedirectUri(PASSWORD_UPDATE_FORM);

		final HashMap<String, String> requestParams = new HashMap<>();
		requestParams.put("Authorization", "Bearer " + cookieService.getCampaignManagerToken());
		requestParams.put("Content-Type", "application/json");
		serverRequest.setRequestParameters(requestParams);

		return httpRequestHandlerService.sendAndHandleRequest(serverRequest, passwordUpdateForm, errors).getRedirectUri();
	}

	@GetMapping(CAMPAIGNS_OVERVIEW_URI)
	public String getCampaignsOverview(final Model model) {
		final ServerRequest serverRequest = new ServerRequest();
		serverRequest.setRelativeUri("/campaign-manager/campaigns");
		serverRequest.setMethod("GET");
		serverRequest.setSuccessRedirectUri(CAMPAIGNS_OVERVIEW_FORM);
		serverRequest.setErrorRedirectUri("error");
		serverRequest.setResponseBodyType(new TypeReference<List<CampaignDTO>>() {
		});

		final HashMap<String, String> requestParams = new HashMap<>();
		requestParams.put("Authorization", "Bearer " + cookieService.getCampaignManagerToken());
		serverRequest.setRequestParameters(requestParams);

		final ServerResponse response = httpRequestHandlerService.sendAndHandleRequest(serverRequest, null, null);

		model.addAttribute("campaigns", response.getBody());
		return response.getRedirectUri();
	}
}
