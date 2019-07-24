package com.greenapper.dtos;

public class ImageDTO {
	private byte[] image;

	public ImageDTO() {

	}

	public ImageDTO(byte[] image) {
		this.image = image;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
}
