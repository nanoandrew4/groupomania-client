package com.greenapper.dtos.campaign;

import com.greenapper.enums.CampaignType;

public class OfferCampaignDTO extends CampaignDTO {
	public OfferCampaignDTO() {
		this.setType(CampaignType.OFFER);
	}
}
