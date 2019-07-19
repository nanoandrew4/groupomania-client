package com.greenapper.controllers.campaign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.greenapper.controllers.CampaignManagerController;
import com.greenapper.dtos.ServerRequest;
import com.greenapper.dtos.ServerResponse;
import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.services.CookieService;
import com.greenapper.services.HttpRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

@Controller
public class DefaultCampaignController {

	@Autowired
	private HttpRequestService httpRequestService;

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
		try {
			final List<CampaignDTO> campaigns = httpRequestService.getObjectMapper()
					.readValue(new URL("http://localhost:8444/api/campaigns"), new TypeReference<List<CampaignDTO>>() {
					});
			model.addAttribute("campaigns", campaigns);
		} catch (IOException e) {
			LOG.error("Could not retrieve all visible campaigns");
		}
		return "home";
	}

	@GetMapping(CAMPAIGN_VIEW_URI + "/{id}")
	public String getCampaignForViewById(@PathVariable final Long id, final Model model) {
		try {
			final CampaignDTO campaignDTO = httpRequestService.getObjectMapper()
					.readValue(new URL("http://localhost:8444/api/campaigns/" + id), CampaignDTO.class);
			model.addAttribute("campaign", campaignDTO);
			model.addAttribute("readonly", true);
			return "campaigns/" + campaignDTO.getType().displayName.toLowerCase() + "Campaign";
		} catch (IOException e) {
			LOG.error("Could not retrieve campaign by ID with id: " + id);
			return "error";
		}
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

		final ServerResponse response = httpRequestService.sendAndHandleRequest(serverRequest, null, null);

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

		return httpRequestService.sendAndHandleRequest(serverRequest, null, null).getRedirectUri();
	}
}
