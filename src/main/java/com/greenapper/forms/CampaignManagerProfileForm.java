package com.greenapper.forms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

public class CampaignManagerProfileForm {
	private Long id;

	private String name;

	private String email;

	private String address;

	@JsonIgnore
	private MultipartFile tempProfileImage;

	private ImageForm profileImage;

	private String profileImageFilePath;

	private CampaignManagerProfileForm() {
	}

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

	public MultipartFile getTempProfileImage() {
		return tempProfileImage;
	}

	public void setTempProfileImage(MultipartFile tempProfileImage) {
		this.tempProfileImage = tempProfileImage;
		setProfileImage(new ImageForm(tempProfileImage));
	}

	public ImageForm getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(ImageForm profileImage) {
		this.profileImage = profileImage;
	}

	public String getProfileImageFilePath() {
		return profileImageFilePath;
	}

	public void setProfileImageFilePath(String profileImageFilePath) {
		this.profileImageFilePath = profileImageFilePath;
	}
}

