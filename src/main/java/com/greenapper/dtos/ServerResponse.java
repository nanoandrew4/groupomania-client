package com.greenapper.dtos;

import java.util.List;
import java.util.Map;

public class ServerResponse {
	private int code;

	private Map<String, List<String>> headers;

	private Object body;

	private String redirectUri;

	public ServerResponse() {}

	public ServerResponse(final int code, final Map<String, List<String>> headers, final Object body, final String redirectUri) {
		this.code = code;
		this.headers = headers;
		this.body = body;
		this.redirectUri = redirectUri;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
}
