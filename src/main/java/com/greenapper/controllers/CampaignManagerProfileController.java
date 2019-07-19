package com.greenapper.controllers;

import org.springframework.stereotype.Controller;

@Controller
public class CampaignManagerProfileController {
	private final static String ROOT_URI = "/campaign-manager/profile";

	public final static String PROFILE_UPDATE_URI = ROOT_URI + "/setup";

	public final static String PROFILE_UPDATE_FORM = "campaign_manager/profileSetup";

	public final static String PROFILE_UPDATE_SUCCESS = "redirect:" + CampaignManagerController.CAMPAIGNS_OVERVIEW_URI;
}
