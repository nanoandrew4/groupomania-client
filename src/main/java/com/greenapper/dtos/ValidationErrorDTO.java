package com.greenapper.dtos;

import java.util.List;

public class ValidationErrorDTO extends ErrorDTO {

	private List<String> validationErrors;

	public ValidationErrorDTO() {}

	public List<String> getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(final List<String> validationErrors) {
		this.validationErrors = validationErrors;
	}
}