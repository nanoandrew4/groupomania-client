package com.greenapper.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.greenapper.dtos.CampaignManagerProfileDTO;
import com.greenapper.dtos.ServerRequest;
import com.greenapper.dtos.ServerResponse;
import com.greenapper.forms.CampaignManagerProfileForm;
import com.greenapper.services.CookieService;
import com.greenapper.services.HttpRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.HashMap;

@Controller
public class CampaignManagerProfileController {

	@Autowired
	private HttpRequestService httpRequestService;

	@Autowired
	private CookieService cookieService;

	private final static String ROOT_URI = "/campaign-manager/profile";

	public final static String PROFILE_UPDATE_URI = ROOT_URI;

	public final static String PROFILE_UPDATE_FORM = "campaign_manager/profileSetup";

	public final static String PROFILE_UPDATE_SUCCESS = "redirect:" + CampaignManagerController.CAMPAIGNS_OVERVIEW_URI;

	@GetMapping(PROFILE_UPDATE_URI)
	public String getProfileUpdateForm(final Model model) {
		final ServerRequest serverRequest = new ServerRequest();
		serverRequest.setRelativeUri("/campaign-manager/profile");
		serverRequest.setMethod("GET");
		serverRequest.setSuccessRedirectUri(PROFILE_UPDATE_FORM);
		serverRequest.setErrorRedirectUri(CampaignManagerController.CAMPAIGNS_OVERVIEW_FORM);
		serverRequest.setResponseBodyType(new TypeReference<CampaignManagerProfileDTO>() {
		});

		final HashMap<String, String> requestParams = new HashMap<>();
		requestParams.put("Authorization", "Bearer " + cookieService.getCampaignManagerToken());
		serverRequest.setRequestParameters(requestParams);

		final ServerResponse response = httpRequestService.sendAndHandleRequest(serverRequest, null, null);
		model.addAttribute("campaignManagerProfile", response.getBody());
		return response.getRedirectUri();
	}

	@PutMapping(PROFILE_UPDATE_URI)
	public String updateProfile(final CampaignManagerProfileForm profileForm, final Errors errors) {
		final ServerRequest serverRequest = new ServerRequest();
		serverRequest.setRelativeUri("/campaign-manager/profile");
		serverRequest.setMethod("PUT");
		serverRequest.setSuccessRedirectUri(PROFILE_UPDATE_SUCCESS);
		serverRequest.setErrorRedirectUri(PROFILE_UPDATE_FORM);

		final HashMap<String, String> requestParams = new HashMap<>();
		requestParams.put("Authorization", "Bearer " + cookieService.getCampaignManagerToken());
		requestParams.put("Content-Type", "application/json");
		serverRequest.setRequestParameters(requestParams);

		final ServerResponse response = httpRequestService.sendAndHandleRequest(serverRequest, profileForm, errors);
		return response.getRedirectUri();
	}
}
