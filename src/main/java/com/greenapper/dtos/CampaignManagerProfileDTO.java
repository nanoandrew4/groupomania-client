package com.greenapper.dtos;

public class CampaignManagerProfileDTO {
	private Long campaignManager;

	private String name;

	private String email;

	private String address;

	private String profileImageFilePath;

	public Long getCampaignManager() {
		return campaignManager;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public String getProfileImageFilePath() {
		return profileImageFilePath;
	}
}
