package com.greenapper.forms;

import org.springframework.web.multipart.MultipartFile;

public class CampaignManagerProfileForm {
	private Long id;

	private Long campaignManager;

	private String name;

	private String email;

	private String address;

	private MultipartFile profileImage;

	private CampaignManagerProfileForm() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCampaignManager() {
		return campaignManager;
	}

	public void setCampaignManager(Long campaignManager) {
		this.campaignManager = campaignManager;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public MultipartFile getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(MultipartFile profileImage) {
		this.profileImage = profileImage;
	}
}

