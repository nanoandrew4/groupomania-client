package com.greenapper.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenapper.dtos.ServerRequest;
import com.greenapper.dtos.ServerResponse;
import com.greenapper.dtos.ValidationErrorDTO;
import com.greenapper.services.HttpRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Component
public class DefaultHttpRequestService implements HttpRequestService {

	@Autowired
	private ObjectMapper objectMapper;

	@Value(value = "${groupomania.server.backend.url}")
	private String serverUrl;

	private Logger LOG = LoggerFactory.getLogger(DefaultHttpRequestService.class);

	@Override
	public ServerResponse sendAndHandleRequest(final ServerRequest serverRequest, final Object body, final Errors errors) {
		if (body != null) {
			try {
				serverRequest.setBody(objectMapper.writerFor(body.getClass()).writeValueAsString(body));
			} catch (JsonProcessingException e) {
				LOG.error("Error parsing body for object: " + body, e);
			}
		}

		final ServerResponse response = sendRequest(serverRequest.getRelativeUri(), serverRequest.getMethod(),
													serverRequest.getRequestParameters(), serverRequest.getBody());

		if (response.getCode() >= 200 && response.getCode() < 300) {
			response.setRedirectUri(serverRequest.getSuccessRedirectUri());

			if (!serverRequest.getResponseBodyType().getType().equals(new TypeReference<String>() {
			}.getType()))
				response.setBody(parseResponseBody(serverRequest.getResponseBodyType(), (String) response.getBody()));
		} else if (response.getCode() < 400) {
			response.setRedirectUri("redirect:" + response.getHeaders().get("Location").get(0).replaceAll(serverUrl, ""));
		} else if (response.getCode() == 401) {
			response.setRedirectUri("redirect:/login");
		} else if (response.getCode() == 404) {
			response.setRedirectUri("error-not-found");
		} else if (response.getCode() >= 400) {
			final ValidationErrorDTO validationErrorDTO = parseValidationErrors((String) response.getBody());
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
	public ServerResponse sendRequest(final String relativeUri, final String method,
									  final Map<String, String> requestProperties, final String body) {
		HttpURLConnection conn = null;
		final ServerResponse serverResponse = new ServerResponse();
		try {
			final URL url = new URL(serverUrl + relativeUri);
			conn = (HttpURLConnection) url.openConnection();

			if ("PATCH".equals(method)) {
				conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
				conn.setRequestMethod("POST");
			} else
				conn.setRequestMethod(method);

			conn.setInstanceFollowRedirects(false);
			if (requestProperties != null && requestProperties.size() > 0)
				for (String key : requestProperties.keySet())
					conn.addRequestProperty(key, requestProperties.get(key));

			if (body != null && body.trim().length() > 0) {
				conn.setDoOutput(true);
				final OutputStream os = conn.getOutputStream();

				os.write(body.getBytes());
				os.flush();
				os.close();
			}

			serverResponse.setCode(conn.getResponseCode());
			serverResponse.setBody(readStream(conn.getInputStream()));
			serverResponse.setHeaders(conn.getHeaderFields());
		} catch (IOException e) {
			try {
				if (conn != null)
					serverResponse.setBody(readStream(conn.getErrorStream()));
			} catch (IOException ex) {
				LOG.error("An error occurred, neither a success or error response stream were available", ex);
			}
		}

		return serverResponse;
	}

	@Override
	public Object parseResponseBody(final TypeReference typeReference, final String body) {
		try {
			return objectMapper.readValue(body, typeReference);
		} catch (IOException e) {
			LOG.error("Error converting to type: " + typeReference + " with body: " + body);
			return body;
		}
	}

	@Override
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	@Override
	public ValidationErrorDTO parseValidationErrors(final String body) {
		try {
			return objectMapper.readValue(body, ValidationErrorDTO.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String readStream(final InputStream inputStream) throws IOException {
		final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		final StringBuilder responseBuffer = new StringBuilder();

		String line;
		while ((line = in.readLine()) != null)
			responseBuffer.append(line);
		return responseBuffer.toString();
	}
}
