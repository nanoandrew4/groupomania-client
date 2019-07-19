package com.greenapper.dtos;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class ServerRequest {
	private String method;

	private String relativeUri;

	private Map<String, String> requestParameters;

	private String body;

	private TypeReference responseBodyType = new TypeReference<String>() {
	};

	private String successRedirectUri;

	private String errorRedirectUri;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRelativeUri() {
		return relativeUri;
	}

	public void setRelativeUri(String relativeUri) {
		this.relativeUri = relativeUri;
	}

	public Map<String, String> getRequestParameters() {
		return requestParameters;
	}

	public void setRequestParameters(Map<String, String> requestParameters) {
		this.requestParameters = requestParameters;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSuccessRedirectUri() {
		return successRedirectUri;
	}

	public void setSuccessRedirectUri(String successRedirectUri) {
		this.successRedirectUri = successRedirectUri;
	}

	public String getErrorRedirectUri() {
		return errorRedirectUri;
	}

	public void setErrorRedirectUri(String errorRedirectUri) {
		this.errorRedirectUri = errorRedirectUri;
	}

	@Override
	public String toString() {
		return "Method: " + method + "\nRelativeUri: " + relativeUri + "\nRequestParameters: " + requestParameters
			   + "\nBody: " + body;
	}

	public TypeReference getResponseBodyType() {
		return responseBodyType;
	}

	public void setResponseBodyType(TypeReference responseBodyType) {
		this.responseBodyType = responseBodyType;
	}
}
