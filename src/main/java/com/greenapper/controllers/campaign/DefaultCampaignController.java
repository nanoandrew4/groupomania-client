package com.greenapper.controllers.campaign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenapper.dtos.campaign.CampaignDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Controller
@RequestMapping("/campaigns")
public class DefaultCampaignController {

	private final ObjectMapper objectMapper = new ObjectMapper();

	private Logger LOG = LoggerFactory.getLogger(DefaultCampaignController.class);

	@GetMapping
	public String getAllVisibleCampaigns(final Model model) {
		try {
			model.addAttribute("campaigns", objectMapper.readValue(new URL("http://localhost:8444/api/campaigns"), new TypeReference<List<CampaignDTO>>() {
			}));
		} catch (IOException e) {
			LOG.error("Could not retrieve all visible campaigns");
		}
		return "home";
	}

	@GetMapping("/{id}")
	public String getCampaignById(@PathVariable final Long id, final Model model) {
		try {
			final CampaignDTO campaignDTO = objectMapper.readValue(new URL("http://localhost:8444/api/campaigns/" + id), CampaignDTO.class);
			model.addAttribute("campaign", campaignDTO);
			model.addAttribute("readonly", true);
			return "campaigns/" + campaignDTO.getType().displayName.toLowerCase() + "Campaign";
		} catch (IOException e) {
			LOG.error("Could not retrieve campaign by ID with id: " + id);
			return "error";
		}
	}
}
