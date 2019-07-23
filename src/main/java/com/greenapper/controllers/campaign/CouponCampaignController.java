package com.greenapper.controllers.campaign;

import com.greenapper.dtos.ServerRequest;
import com.greenapper.forms.campaigns.CouponCampaignForm;
import com.greenapper.services.CookieService;
import com.greenapper.services.HttpRequestHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.HashMap;

@Controller
public class CouponCampaignController {

	@Autowired
	private HttpRequestHandlerService httpRequestHandlerService;

	@Autowired
	private CookieService cookieService;

	public final static String ROOT_URI = "/campaigns/coupon";

	public final static String CAMPAIGN_CREATION_URI = ROOT_URI + "/create";

	public final static String CAMPAIGN_CREATION_SUCCESS_REDIRECT = "redirect:/campaign-manager/campaigns";

	public final static String COUPON_CAMPAIGN_FORM = "campaigns/couponCampaign";

	public final static String CAMPAIGN_UPDATE_URI = ROOT_URI + "/update";

	public final static String CAMPAIGN_UPDATE_SUCCESS_REDIRECT = "redirect:/campaign-manager/campaigns";

	@GetMapping(CAMPAIGN_CREATION_URI)
	public String getEmptyForm(@ModelAttribute("campaign") final CouponCampaignForm couponCampaignForm) {
		return COUPON_CAMPAIGN_FORM;
	}

	@PostMapping(CAMPAIGN_CREATION_URI)
	public String createCampaign(@ModelAttribute("campaign") final CouponCampaignForm couponCampaignForm, final Errors errors) {
		final ServerRequest serverRequest = new ServerRequest();
		serverRequest.setRelativeUri("/campaigns/coupon/create");
		serverRequest.setMethod("POST");

		final HashMap<String, String> requestParams = new HashMap<>();
		requestParams.put("Authorization", "Bearer " + cookieService.getCampaignManagerToken());
		requestParams.put("Content-Type", "application/json");
		serverRequest.setRequestParameters(requestParams);

		serverRequest.setSuccessRedirectUri(CAMPAIGN_CREATION_SUCCESS_REDIRECT);
		serverRequest.setErrorRedirectUri(COUPON_CAMPAIGN_FORM);

		return httpRequestHandlerService.sendAndHandleRequest(serverRequest, couponCampaignForm, errors).getRedirectUri();
	}

	@PutMapping(CAMPAIGN_UPDATE_URI)
	public String updateCampaign(@ModelAttribute("campaign") final CouponCampaignForm couponCampaignForm, final Errors errors) {
		final ServerRequest serverRequest = new ServerRequest();
		serverRequest.setRelativeUri("/campaigns/coupon/update");
		serverRequest.setMethod("PUT");
		serverRequest.setSuccessRedirectUri(CAMPAIGN_UPDATE_SUCCESS_REDIRECT);
		serverRequest.setErrorRedirectUri(COUPON_CAMPAIGN_FORM);

		final HashMap<String, String> requestParams = new HashMap<>();
		requestParams.put("Authorization", "Bearer " + cookieService.getCampaignManagerToken());
		requestParams.put("Content-Type", "application/json");
		serverRequest.setRequestParameters(requestParams);

		return httpRequestHandlerService.sendAndHandleRequest(serverRequest, couponCampaignForm, errors).getRedirectUri();
	}
}
