package com.greenapper.enums;

/**
 * Enum of possible campaign types. The enum values and display names should match those used in model and form classes,
 * since reflection is used throughout the project, and these types are used for creating and accessing class methods.
 * For example, the {@link #COUPON} campaign type has a display value of 'Coupon'. The model for coupon campaigns is
 * named {@link com.greenapper.models.campaigns.CouponCampaign}, and the form for coupon campaigns
 * {@link com.greenapper.forms.campaigns.CouponCampaignForm}.
 */
public enum CampaignType {
	COUPON("Coupon"), OFFER("Offer");

	public final String displayName;

	CampaignType(final String displayName) {
		this.displayName = displayName;
	}
}
