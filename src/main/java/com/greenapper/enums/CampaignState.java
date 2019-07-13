package com.greenapper.enums;

/**
 * Enum listing possible campaign states.
 */
public enum CampaignState {
	ACTIVE("Active"), INACTIVE("Inactive"), ARCHIVED("Archived");

	public final String displayName;

	CampaignState(final String displayName) {
		this.displayName = displayName;
	}
}
