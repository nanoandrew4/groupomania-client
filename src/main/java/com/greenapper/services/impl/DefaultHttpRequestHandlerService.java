package com.greenapper.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenapper.dtos.ServerRequest;
import com.greenapper.dtos.ServerResponse;
import com.greenapper.dtos.ValidationErrorDTO;
import com.greenapper.services.HttpRequestHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.IOException;

@Component
public class DefaultHttpRequestHandlerService implements HttpRequestHandlerService {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private HttpRequestService httpRequestService;

	@Value(value = "${groupomania.server.backend.url}")
	private String serverUrl;

	private Logger LOG = LoggerFactory.getLogger(DefaultHttpRequestHandlerService.class);

	@Override
	public ServerResponse sendAndHandleRequest(final ServerRequest serverRequest, final Object body, final Errors errors) {
		if (body != null) {
			try {
				serverRequest.setBody(objectMapper.writerFor(body.getClass()).writeValueAsString(body));
			} catch (JsonProcessingException e) {
				LOG.error("Error parsing body for object: " + body, e);
			}
		}

		final ServerResponse response = httpRequestService.sendRequest(serverRequest.getRelativeUri(), serverRequest.getMethod(),
																	   serverRequest.getRequestParameters(), serverRequest.getBody());

		if (response.getCode() >= 200 && response.getCode() < 300) {
			response.setRedirectUri(serverRequest.getSuccessRedirectUri());

			if (!serverRequest.getResponseBodyType().getType().equals(new TypeReference<String>() {
			}.getType()))
				response.setBody(parseBody(serverRequest.getResponseBodyType(), (String) response.getBody()));
		} else if (response.getCode() >= 300 && response.getCode() < 400) {
			response.setRedirectUri("redirect:" + response.getHeaders().get("Location").get(0).replaceAll(serverUrl, ""));
		} else if (response.getCode() == 401) {
			response.setRedirectUri("redirect:/login");
		} else if (response.getCode() == 404) {
			response.setRedirectUri("error-not-found");
		} else if (response.getCode() >= 400) {
			final ValidationErrorDTO validationErrorDTO = (ValidationErrorDTO) parseBody(new TypeReference<ValidationErrorDTO>() {
			}, (String) response.getBody());
			if (validationErrorDTO != null && validationErrorDTO.getValidationErrors() != null && errors != null) {
				for (String error : validationErrorDTO.getValidationErrors())
					errors.reject(null, error);
			}

			response.setRedirectUri(serverRequest.getErrorRedirectUri());
		} else {
			LOG.error("Unable to handle request: " + serverRequest);
		}

		return response;
	}

	@Override
	public Object parseBody(final TypeReference typeReference, final String body) {
		try {
			return objectMapper.readValue(body, typeReference);
		} catch (IOException e) {
			LOG.error("Error converting to type: " + typeReference + " with body: " + body, e);
			return body;
		}
	}

	@Override
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
}
