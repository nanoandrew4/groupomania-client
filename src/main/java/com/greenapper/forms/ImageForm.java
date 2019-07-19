package com.greenapper.forms;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ImageForm {
	private String name;

	private String type;

	private Long size;

	private byte[] bytes;

	public ImageForm(final MultipartFile multipartFile) {
		this.name = multipartFile.getName();
		this.type = multipartFile.getContentType();
		this.size = multipartFile.getSize();
		try {
			this.bytes = multipartFile.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
