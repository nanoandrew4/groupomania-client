package com.greenapper.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenapper.dtos.ValidationErrorDTO;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.services.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
@RequestMapping("/campaign-manager/")
public class CampaignManagerController {

	@Autowired
	private CookieService cookieService;

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("/password")
	public String getUpdatePasswordForm(final PasswordUpdateForm passwordUpdateForm) {
		return "campaign_manager/passwordUpdate";
	}

	@PatchMapping("/password")
	public String updatePassword(final PasswordUpdateForm passwordUpdateForm, final Errors errors) {
		try {
			final String body = objectMapper.writerFor(PasswordUpdateForm.class).writeValueAsString(passwordUpdateForm);
			final String responseBody = sendRequest("http://localhost:8444/api/campaign-manager/password", "PUT", body);
			if (responseBody == null || responseBody.trim().length() == 0)
				return "redirect:/campaign-manager/campaigns";

			final ValidationErrorDTO validationErrorDTO = parseValidationErrors(responseBody);
			if (validationErrorDTO != null && validationErrorDTO.getValidationErrors() != null)
				for (String ex : validationErrorDTO.getValidationErrors())
					errors.reject(null, ex);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "campaign_manager/passwordUpdate";
	}

	private String sendRequest(final String urlStr, final String method, final String body) {
		HttpURLConnection conn = null;
		try {
			final URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();

			if ("PATCH".equals(method)) {
				conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
				conn.setRequestMethod("POST");
			} else
				conn.setRequestMethod(method);

			conn.addRequestProperty("Authorization", "Bearer " + cookieService.getCampaignManagerToken());

			if (body != null && body.trim().length() > 0) {
				conn.addRequestProperty("Content-Type", "application/json");

				conn.setDoOutput(true);
				final OutputStream os = conn.getOutputStream();

				os.write(body.getBytes());
				os.flush();
				os.close();
			}

			return readStream(conn.getInputStream());
		} catch (IOException e) {
			try {
				if (conn != null)
					return readStream(conn.getErrorStream());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}

	private String readStream(final InputStream inputStream) throws IOException {
		final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		final StringBuilder responseBuffer = new StringBuilder();

		String line;
		while ((line = in.readLine()) != null)
			responseBuffer.append(line);
		return responseBuffer.toString();
	}

	private ValidationErrorDTO parseValidationErrors(final String body) {
		try {
			return objectMapper.readValue(body, ValidationErrorDTO.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
