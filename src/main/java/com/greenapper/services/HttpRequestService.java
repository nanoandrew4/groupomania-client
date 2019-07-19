package com.greenapper.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenapper.dtos.ServerRequest;
import com.greenapper.dtos.ServerResponse;
import com.greenapper.dtos.ValidationErrorDTO;
import org.springframework.validation.Errors;

import java.util.Map;

public interface HttpRequestService {
	ServerResponse sendAndHandleRequest(final ServerRequest serverRequest, final Object body, final Errors errors);

	ServerResponse sendRequest(final String relativeUri, final String method, final Map<String, String> requestProperties, final String body);

	Object parseResponseBody(final TypeReference typeReference, final String body);

	ValidationErrorDTO parseValidationErrors(final String body);

	ObjectMapper getObjectMapper();
}
