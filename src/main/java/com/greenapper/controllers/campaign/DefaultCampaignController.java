package com.greenapper.controllers.campaign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.greenapper.controllers.CampaignManagerController;
import com.greenapper.dtos.ServerRequest;
import com.greenapper.dtos.ServerResponse;
import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.services.CookieService;
import com.greenapper.services.HttpRequestHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.HashMap;
import java.util.List;

@Controller
public class DefaultCampaignController {

	@Autowired
	private HttpRequestHandlerService httpRequestHandlerService;

	@Autowired
	private CookieService cookieService;

	private Logger LOG = LoggerFactory.getLogger(DefaultCampaignController.class);

	private final static String ROOT_URI = "/campaigns";

	public final static String CAMPAIGN_VIEW_URI = ROOT_URI + "/view";

	public final static String CAMPAIGN_EDIT_URI = ROOT_URI + "/edit";

	public final static String CAMPAIGN_STATE_UPDATE_URI = ROOT_URI + "/state";

	public final static String CAMPAIGN_STATE_UPDATE_SUCCESS_REDIRECT = "redirect:" + CampaignManagerController.CAMPAIGNS_OVERVIEW_URI;

	@GetMapping(ROOT_URI)
	public String getAllVisibleCampaigns(final Model model) {
		final ServerRequest serverRequest = new ServerRequest();
		serverRequest.setRelativeUri("/campaigns");
		serverRequest.setMethod("GET");
		serverRequest.setSuccessRedirectUri("home");
		serverRequest.setErrorRedirectUri("home");
		serverRequest.setResponseBodyType(new TypeReference<List<CampaignDTO>>() {
		});

		final ServerResponse response = httpRequestHandlerService.sendAndHandleRequest(serverRequest, null, null);

		model.addAttribute("campaigns", response.getBody());
		return response.getRedirectUri();
	}

	@GetMapping(CAMPAIGN_VIEW_URI + "/{id}")
	public String getCampaignForViewById(@PathVariable final Long id, final Model model) {
		final ServerRequest serverRequest = new ServerRequest();
		serverRequest.setRelativeUri("/campaigns/" + id);
		serverRequest.setMethod("GET");
		serverRequest.setSuccessRedirectUri(null);
		serverRequest.setErrorRedirectUri("error");
		serverRequest.setResponseBodyType(new TypeReference<CampaignDTO>() {
		});

		final HashMap<String, String> requestParams = new HashMap<>();
		final String token = cookieService.getCampaignManagerToken();
		if (token != null)
			requestParams.put("Authorization", "Bearer " + token);
		serverRequest.setRequestParameters(requestParams);

		final ServerResponse response = httpRequestHandlerService.sendAndHandleRequest(serverRequest, null, null);

		model.addAttribute("campaign", response.getBody());
		model.addAttribute("readonly", true);
		String redirectUri = response.getRedirectUri();
		if (redirectUri == null)
			redirectUri = "campaigns/" + ((CampaignDTO) response.getBody()).getType().displayName.toLowerCase() + "Campaign";
		return redirectUri;
	}

	@GetMapping(CAMPAIGN_EDIT_URI + "/{id}")
	public String getCampaignForEditById(@PathVariable final Long id, final Model model) {
		final ServerRequest serverRequest = new ServerRequest();
		serverRequest.setRelativeUri("/campaigns/" + id);
		serverRequest.setMethod("GET");
		serverRequest.setSuccessRedirectUri(null);
		serverRequest.setErrorRedirectUri("error");
		serverRequest.setResponseBodyType(new TypeReference<CampaignDTO>() {
		});

		final HashMap<String, String> requestParams = new HashMap<>();
		requestParams.put("Authorization", "Bearer " + cookieService.getCampaignManagerToken());
		serverRequest.setRequestParameters(requestParams);

		final ServerResponse response = httpRequestHandlerService.sendAndHandleRequest(serverRequest, null, null);

		model.addAttribute("campaign", response.getBody());
		String redirectUri = response.getRedirectUri();
		if (redirectUri == null)
			redirectUri = "campaigns/" + ((CampaignDTO) response.getBody()).getType().displayName.toLowerCase() + "Campaign";
		return redirectUri;
	}

	@PutMapping(CAMPAIGN_STATE_UPDATE_URI + "/{id}/{state}")
	public String updateCampaignState(@PathVariable final Long id, @PathVariable final String state) {
		final ServerRequest serverRequest = new ServerRequest();
		serverRequest.setRelativeUri("/campaigns/state/" + id + "/" + state);
		serverRequest.setMethod("PUT");
		serverRequest.setSuccessRedirectUri(CAMPAIGN_STATE_UPDATE_SUCCESS_REDIRECT);
		serverRequest.setErrorRedirectUri(CampaignManagerController.CAMPAIGNS_OVERVIEW_FORM);

		final HashMap<String, String> requestParams = new HashMap<>();
		requestParams.put("Authorization", "Bearer " + cookieService.getCampaignManagerToken());
		serverRequest.setRequestParameters(requestParams);

		return httpRequestHandlerService.sendAndHandleRequest(serverRequest, null, null).getRedirectUri();
	}
}
