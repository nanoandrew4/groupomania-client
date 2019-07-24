package com.greenapper.services.impl;

import com.greenapper.dtos.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Component
public class HttpRequestService {

	@Value(value = "${groupomania.server.backend.url}")
	private String serverUrl;

	private Logger LOG = LoggerFactory.getLogger(HttpRequestService.class);

	public ServerResponse sendRequest(final String relativeUri, final String method,
									  @Nullable final Map<String, String> requestProperties, @Nullable final String body) {
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
		} catch (IOException | NullPointerException e) {
			try {
				if (conn != null)
					serverResponse.setBody(readStream(conn.getErrorStream()));
			} catch (IOException | NullPointerException ex) {
				LOG.error("Server response code was: " + serverResponse.getCode() + " for request to " + serverUrl + relativeUri);
				LOG.error("An error occurred, neither a success or error response stream were available", ex);
			}
		}

		return serverResponse;
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
