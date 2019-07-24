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

/**
 * Class for sending requests and interpreting responses from the groupomania backend.
 */
@Component
public class HttpRequestService {

	@Value(value = "${groupomania.server.backend.url}")
	private String serverUrl;

	private Logger LOG = LoggerFactory.getLogger(HttpRequestService.class);

	/**
	 * Sends a request to the groupomania backend, with the specified parameters. Once the request is submitted,
	 * the following fields of the {@link ServerResponse} instance will be populated: code, body, headers.
	 * The body of the response is read and stored as a plain string, so conversion to objects has to be handled elsewhere
	 * if such functionality is required.
	 *
	 * @param relativeUri       URI without the host and context path of the desired endpoint to call
	 * @param method            HTTP method for the request (GET, POST...)
	 * @param requestProperties Request properties for the call, such as headers
	 * @param body              The body of the request
	 * @return A {@link ServerResponse} instance containing the details of the request
	 */
	public ServerResponse sendRequest(final String relativeUri, final String method,
									  @Nullable final Map<String, String> requestProperties, @Nullable final String body) {
		HttpURLConnection conn = null;
		final ServerResponse serverResponse = new ServerResponse();
		try {
			final URL url = new URL(serverUrl + relativeUri);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);

			conn.setInstanceFollowRedirects(false); // Allows 300 responses to be interpreted locally, instead of automatically resolved
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

	/**
	 * Reads an {@link InputStream} and returns its contents as a {@link String}.
	 *
	 * @param inputStream Input stream to read from
	 * @return Contents of the input stream, as a String
	 *
	 * @throws IOException If the stream is null
	 */
	private String readStream(final InputStream inputStream) throws IOException {
		final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		final StringBuilder responseBuffer = new StringBuilder();

		String line;
		while ((line = in.readLine()) != null)
			responseBuffer.append(line);
		return responseBuffer.toString();
	}
}
