package com.greenapper.dtos.campaign;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.greenapper.enums.CampaignState;
import com.greenapper.enums.CampaignType;
import com.greenapper.jackson.deserializers.CampaignDTODeserializer;

@JsonDeserialize(using = CampaignDTODeserializer.class)
public class CampaignDTO {
	private Long id;

	private String title;

	private String description;

	private String campaignImageFilePath;

	private CampaignType type;

	private CampaignState state;

	private String startDate;

	private String endDate;

	private String quantity = String.valueOf(Double.POSITIVE_INFINITY);

	private boolean showAfterExpiration;

	private String originalPrice;

	private String percentDiscount;

	private String discountedPrice;

	protected CampaignDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CampaignType getType() {
		return type;
	}

	public void setType(CampaignType type) {
		this.type = type;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public boolean isShowAfterExpiration() {
		return showAfterExpiration;
	}

	public void setShowAfterExpiration(boolean showAfterExpiration) {
		this.showAfterExpiration = showAfterExpiration;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getPercentDiscount() {
		return percentDiscount;
	}

	public void setPercentDiscount(String percentDiscount) {
		this.percentDiscount = percentDiscount;
	}

	public String getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(String discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public String getCampaignImageFilePath() {
		return campaignImageFilePath;
	}

	public void setCampaignImageFilePath(String campaignImageFilePath) {
		this.campaignImageFilePath = campaignImageFilePath;
	}

	public CampaignState getState() {
		return state;
	}

	public void setState(CampaignState state) {
		this.state = state;
	}
}
