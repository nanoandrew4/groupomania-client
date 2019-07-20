package com.greenapper.dtos;

public class CampaignManagerProfileDTO {
	private Long id;

	private String name;

	private String email;

	private String address;

	private String profileImageFilePath;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getProfileImageFilePath() {
		return profileImageFilePath;
	}

	public void setProfileImageFilePath(String profileImageFilePath) {
		this.profileImageFilePath = profileImageFilePath;
	}
}
